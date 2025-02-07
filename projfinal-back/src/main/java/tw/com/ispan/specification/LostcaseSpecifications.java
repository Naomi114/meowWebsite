package tw.com.ispan.specification;

import org.springframework.data.jpa.domain.Specification;
import tw.com.ispan.domain.pet.LostCase;

public class LostcaseSpecifications {

    // 案件標題模糊查詢
    public static Specification<LostCase> caseTitleLike(String caseTitle) {
        return (root, query, criteriaBuilder) -> (caseTitle == null || caseTitle.trim().isEmpty()) ? null
                : criteriaBuilder.like(root.get("caseTitle"), "%" + caseTitle + "%");
    }

    // 根據 memberId 模糊查詢
    public static Specification<LostCase> hasMemberIdLike(String memberIdPattern) {
        return (root, query, criteriaBuilder) -> {
            if (memberIdPattern == null || memberIdPattern.trim().isEmpty()) {
                return null;
            }
            String likePattern = "%" + memberIdPattern + "%";
            return criteriaBuilder.like(root.get("member").get("memberId").as(String.class), likePattern);
        };
    }

    // 根據 lostCaseId 模糊查詢
    public static Specification<LostCase> hasCaseIdLike(String caseIdPattern) {
        return (root, query, criteriaBuilder) -> {
            if (caseIdPattern == null || caseIdPattern.trim().isEmpty()) {
                return null;
            }
            String likePattern = "%" + caseIdPattern + "%";
            return criteriaBuilder.like(root.get("lostCaseId").as(String.class), likePattern);
        };
    }

    // 根據城市名稱模糊查詢
    public static Specification<LostCase> hasCityNameLike(String cityName) {
        return (root, query, criteriaBuilder) -> {
            if (cityName == null || cityName.trim().isEmpty()) {
                return null; // 如果城市名稱為空，則不加入條件
            }
            String likePattern = "%" + cityName + "%";
            return criteriaBuilder.like(root.get("city").get("name"), likePattern);
        };
    }

    // 根據鄉鎮區名稱模糊查詢
    public static Specification<LostCase> hasDistrictAreaNameLike(String districtAreaName) {
        return (root, query, criteriaBuilder) -> {
            if (districtAreaName == null || districtAreaName.trim().isEmpty()) {
                return null; // 如果鄉鎮區名稱為空，則不加入條件
            }
            String likePattern = "%" + districtAreaName + "%";
            return criteriaBuilder.like(root.get("districtArea").get("name"), likePattern);
        };
    }
}
