package tw.com.ispan.specification;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import tw.com.ispan.domain.admin.Admin;

public class AdminSpecifications {

    public static Specification<Admin> hasAdminName(String adminName) {
        return (Root<Admin> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (adminName == null || adminName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("adminName"), adminName);
        };
    }
}
