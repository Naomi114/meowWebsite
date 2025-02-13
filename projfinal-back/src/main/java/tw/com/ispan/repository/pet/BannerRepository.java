package tw.com.ispan.repository.pet;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tw.com.ispan.domain.pet.banner.Banner;
import tw.com.ispan.domain.pet.banner.BannerType;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Integer> {

        // 查詢所有 Banner，並根據 onlineDate 降序排列
        List<Banner> findAllByOrderByOnlineDateDesc();

        // 🔹 透過 LostCaseId 查找 Banner，並確保 bannerType 為 LOST
        @Query("SELECT b FROM Banner b WHERE b.lostCase.lostCaseId = :caseId AND b.bannerType = :bannerType")
        Optional<Banner> findByLostCaseId(@Param("caseId") Integer caseId, @Param("bannerType") BannerType bannerType);

        // 🔹 透過 RescueCaseId 查找 Banner，並確保 bannerType 為 RESCUE
        @Query("SELECT b FROM Banner b WHERE b.rescueCase.rescueCaseId = :caseId AND b.bannerType = :bannerType")
        Optional<Banner> findByRescueCaseId(@Param("caseId") Integer caseId,
                        @Param("bannerType") BannerType bannerType);

        // 🔹 透過 AdoptionCaseId 查找 Banner，並確保 bannerType 為 ADOPT
        @Query("SELECT b FROM Banner b WHERE b.adoptionCase.adoptionCaseId = :caseId AND b.bannerType = :bannerType")
        Optional<Banner> findByAdoptionCaseId(@Param("caseId") Integer caseId,
                        @Param("bannerType") BannerType bannerType);

        // 🔹 根據到期日查詢 Banner
        List<Banner> findByDueDateBefore(LocalDateTime now);

        // 🔹 根據 Banner 類型查詢
        List<Banner> findByBannerType(BannerType bannerType);

        // 🔹 刪除指定案件的 Banner
        void deleteByLostCase_LostCaseIdAndBannerType(Integer caseId, BannerType bannerType);

        void deleteByRescueCase_RescueCaseIdAndBannerType(Integer caseId, BannerType bannerType);

        void deleteByAdoptionCase_AdoptionCaseIdAndBannerType(Integer caseId, BannerType bannerType);

}