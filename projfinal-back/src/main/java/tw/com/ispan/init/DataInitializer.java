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
            System.out.println("CaseView 資料已達上限 (650 筆)，不執行初始化。");
            return;
        }

        // 獲取 rescueCaseId 介於 1-13 的案件
        List<RescueCase> rescueCases = rescueCaseRepository.findByRescueCaseIdBetween(1, 13);

        int remainingSlots = MAX_TOTAL_VIEWS - (int) currentCount;
        int totalInserted = 0;

        for (RescueCase rescueCase : rescueCases) {
            if (remainingSlots <= 0)
                break;

            int viewCount = random.nextInt(MAX_CASE_VIEWS - MIN_CASE_VIEWS + 1) + MIN_CASE_VIEWS;

            // 如果剩餘可插入的數據比隨機數小，則限制為剩餘可插入的數據量
            viewCount = Math.min(viewCount, remainingSlots);

            IntStream.range(0, viewCount).forEach(i -> {
                CaseView caseView = new CaseView();
                caseView.setRescueCase(rescueCase);
                caseView.setViewTime(generateRandomDateTime());

                caseViewRepository.save(caseView);
            });

            totalInserted += viewCount;
            remainingSlots -= viewCount;

            System.out.println("已為案件 " + rescueCase.getRescueCaseId() + " 插入 " + viewCount + " 筆瀏覽數據");
        }

        System.out.println("初始化完成，總共插入 " + totalInserted + " 筆 CaseView 資料。");
    }

    // 產生 2025/1/19 ~ 2025/2/19 之間的隨機日期時間
    private LocalDateTime generateRandomDateTime() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 19, 0, 0);
        LocalDateTime end = LocalDateTime.of(2025, 2, 19, 23, 59);

        long secondsBetween = ChronoUnit.SECONDS.between(start, end);
        return start.plusSeconds(random.nextLong(secondsBetween));
    }
}
