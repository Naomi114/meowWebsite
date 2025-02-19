package tw.com.ispan.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tw.com.ispan.domain.pet.CaseView;
import tw.com.ispan.domain.pet.RescueCase;
import tw.com.ispan.repository.pet.CaseViewRepository;
import tw.com.ispan.repository.pet.RescueCaseRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Transactional
@Component
@Order(5)
public class DataInitializer implements CommandLineRunner {

    private static final int MAX_TOTAL_VIEWS = 650; // CaseView 總數上限
    private static final int MIN_CASE_VIEWS = 10; // 每個案件最少瀏覽數
    private static final int MAX_CASE_VIEWS = 70; // 每個案件最多瀏覽數
    private static final int FIXED_CASE_VIEWS = 108; // rescueCaseId = 2 的固定新增數
    private static final LocalDateTime HIGH_TRAFFIC_DATE = LocalDateTime.of(2025, 2, 19, 0, 0); // 2/19 高流量日期

    private final CaseViewRepository caseViewRepository;
    private final RescueCaseRepository rescueCaseRepository;
    private final Random random = new Random();

    public DataInitializer(CaseViewRepository caseViewRepository, RescueCaseRepository rescueCaseRepository) {
        this.caseViewRepository = caseViewRepository;
        this.rescueCaseRepository = rescueCaseRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        long currentCount = caseViewRepository.count();

        if (currentCount >= MAX_TOTAL_VIEWS) {
            System.out.println("⚠️ CaseView 資料已達上限 (650 筆)，不執行初始化。");
            return;
        }

        // 獲取 rescueCaseId 介於 1-13 的案件
        List<RescueCase> rescueCases = rescueCaseRepository.findByRescueCaseIdBetween(1, 13);

        int remainingSlots = MAX_TOTAL_VIEWS - (int) currentCount;
        int totalInserted = 0;

        for (RescueCase rescueCase : rescueCases) {
            if (remainingSlots <= 0)
                break;

            int viewCount;

            // 🎯 處理案件 ID = 2 的特殊情況
            if (rescueCase.getRescueCaseId() == 2) {
                viewCount = Math.min(FIXED_CASE_VIEWS, remainingSlots);
                int highTrafficCount = (int) (viewCount * 0.7); // 70% 在 2/19
                int scatteredCount = viewCount - highTrafficCount; // 30% 隨機分佈

                insertCaseViews(rescueCase, highTrafficCount, true); // 2/19 高峰流量
                insertCaseViews(rescueCase, scatteredCount, false); // 其他日期散佈
            } else {
                // 📌 其他案件：隨機插入 10-70 筆
                viewCount = random.nextInt(MAX_CASE_VIEWS - MIN_CASE_VIEWS + 1) + MIN_CASE_VIEWS;
                viewCount = Math.min(viewCount, remainingSlots);
                insertCaseViews(rescueCase, viewCount, false);
            }

            totalInserted += viewCount;
            remainingSlots -= viewCount;

            System.out.println("✅ 已為案件 " + rescueCase.getRescueCaseId() + " 插入 " + viewCount + " 筆瀏覽數據");
        }

        // 🔄 更新 rescueCase 表中的 viewCount
        updateRescueCaseViewCounts();

        System.out.println("🎉 初始化完成，總共插入 " + totalInserted + " 筆 CaseView 資料。");
    }

    // ✅ 插入 CaseView 數據
    private void insertCaseViews(RescueCase rescueCase, int viewCount, boolean isHighTraffic) {
        IntStream.range(0, viewCount).forEach(i -> {
            CaseView caseView = new CaseView();
            caseView.setRescueCase(rescueCase);
            caseView.setViewTime(isHighTraffic ? generateHighTrafficDateTime() : generateRandomDateTime());
            caseViewRepository.save(caseView);
        });
    }

    // 📅 產生 2025/1/19 ~ 2025/2/18 之間的隨機日期時間
    private LocalDateTime generateRandomDateTime() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 19, 0, 0);
        LocalDateTime end = LocalDateTime.of(2025, 2, 18, 23, 59);

        long secondsBetween = ChronoUnit.SECONDS.between(start, end);
        return start.plusSeconds(random.nextLong(secondsBetween));
    }

    // 📌 產生 2/19 之間的高流量日期時間
    private LocalDateTime generateHighTrafficDateTime() {
        return HIGH_TRAFFIC_DATE.plusSeconds(random.nextInt(86400)); // 讓時間分佈在 2/19 一整天
    }

    // 🔄 更新 RescueCase 表中的 viewCount
    private void updateRescueCaseViewCounts() {
        List<RescueCase> rescueCases = rescueCaseRepository.findByRescueCaseIdBetween(1, 13);
        for (RescueCase rescueCase : rescueCases) {
            int viewCount = caseViewRepository.countByRescueCaseId(rescueCase.getRescueCaseId());
            rescueCase.setViewCount(viewCount);
            rescueCaseRepository.save(rescueCase);
            System.out.println("📌 更新案件 " + rescueCase.getRescueCaseId() + " 瀏覽數為 " + viewCount);
        }
    }
}
