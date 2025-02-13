package tw.com.ispan.repository.pet;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import tw.com.ispan.domain.pet.ReportCase;

public class ReportCaseDAOImpl implements ReportCaseDAO {

    @PersistenceContext
    private EntityManager entityManager = null;

    public EntityManager getSession() {
        return entityManager;
    }

    @Override
    public Long count(JSONObject param) {
        // 解析傳入的 JSON 參數
        Integer rescueCaseId = param.isNull("rescueCaseId") ? null : param.getInt("rescueCaseId");
        Integer lostCaseId = param.isNull("lostCaseId") ? null : param.getInt("lostCaseId");
        Integer adoptionCaseId = param.isNull("adoptionCaseId") ? null : param.getInt("adoptionCaseId");
        LocalDateTime reportDate = param.isNull("reportDate") ? null
                : LocalDateTime.parse(param.getString("reportDate"));
        String reportTitle = param.isNull("reportTitle") ? null : param.getString("reportTitle");
        Boolean reportState = param.isNull("reportState") ? null : param.getBoolean("reportState");

        // 確定唯一的 caseId
        Integer caseId = null;
        String caseType = null; // 用於標識是 rescueCase, lostCase 還是 adoptionCase
        if (rescueCaseId != null) {
            caseId = rescueCaseId;
            caseType = "rescueCase";
        } else if (lostCaseId != null) {
            caseId = lostCaseId;
            caseType = "lostCase";
        } else if (adoptionCaseId != null) {
            caseId = adoptionCaseId;
            caseType = "adoptionCase";
        }

        // 構建 Criteria 查詢
        CriteriaBuilder criteriaBuilder = this.getSession().getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<ReportCase> root = criteriaQuery.from(ReportCase.class);

        // select count(*)
        criteriaQuery.select(criteriaBuilder.count(root));

        // 動態構建 WHERE 條件
        List<Predicate> predicates = new ArrayList<>();

        // 根據唯一 caseId 添加條件
        if (caseId != null && caseType != null) {
            predicates.add(criteriaBuilder.equal(root.get(caseType).get(caseType + "Id"), caseId));
        }
        if (reportDate != null) {
            predicates.add(criteriaBuilder.equal(root.get("reportDate"), reportDate));
        }
        if (reportTitle != null && !reportTitle.trim().isEmpty()) {
            String likePattern = "%" + reportTitle + "%";
            predicates.add(criteriaBuilder.like(root.get("reportTitle"), likePattern));
        }
        if (reportState != null) {
            predicates.add(criteriaBuilder.equal(root.get("reportState"), reportState));
        }

        // 應用所有條件
        if (!predicates.isEmpty()) {
            criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        }

        // 執行查詢
        TypedQuery<Long> query = this.getSession().createQuery(criteriaQuery);
        return query.getSingleResult();
    }

    @Override
    public List<ReportCase> find(JSONObject param) {
        System.out.println("param=" + param);

        // 解析傳入的 JSON 參數
        Integer rescueCaseId = param.isNull("rescueCaseId") ? null : param.getInt("rescueCaseId");
        Integer lostCaseId = param.isNull("lostCaseId") ? null : param.getInt("lostCaseId");
        Integer adoptionCaseId = param.isNull("adoptionCaseId") ? null : param.getInt("adoptionCaseId");
        LocalDateTime reportDate = param.isNull("reportDate") ? null
                : LocalDateTime.parse(param.getString("reportDate"));
        String reportTitle = param.isNull("reportTitle") ? null : param.getString("reportTitle");
        Boolean reportState = param.isNull("reportState") ? null : param.getBoolean("reportState");

        Integer start = param.isNull("start") ? null : param.getInt("start"); // 分頁開始索引
        Integer rows = param.isNull("rows") ? 5 : param.getInt("rows"); // 分頁大小
        String sort = param.isNull("sort") ? null : param.getString("sort"); // 排序字段
        boolean dir = param.isNull("dir") ? false : param.getBoolean("dir"); // 排序方向：false=asc，true=desc

        // 確定唯一的 caseId
        Integer caseId = null;
        String caseType = null; // 用於標識是 rescueCase, lostCase 還是 adoptionCase
        if (rescueCaseId != null) {
            caseId = rescueCaseId;
            caseType = "rescueCase";
        } else if (lostCaseId != null) {
            caseId = lostCaseId;
            caseType = "lostCase";
        } else if (adoptionCaseId != null) {
            caseId = adoptionCaseId;
            caseType = "adoptionCase";
        }

        // 構建 Criteria 查詢
        CriteriaBuilder criteriaBuilder = this.getSession().getCriteriaBuilder();
        CriteriaQuery<ReportCase> criteriaQuery = criteriaBuilder.createQuery(ReportCase.class);
        Root<ReportCase> root = criteriaQuery.from(ReportCase.class);

        // 動態構建 WHERE 條件
        List<Predicate> predicates = new ArrayList<>();

        // 根據唯一 caseId 添加條件
        if (caseId != null && caseType != null) {
            predicates.add(criteriaBuilder.equal(root.get(caseType).get(caseType + "Id"), caseId));
        }
        if (reportDate != null) {
            predicates.add(criteriaBuilder.equal(root.get("reportDate"), reportDate));
        }
        if (reportTitle != null && !reportTitle.trim().isEmpty()) {
            String likePattern = "%" + reportTitle + "%";
            predicates.add(criteriaBuilder.like(root.get("reportTitle"), likePattern));
        }
        if (reportState != null) {
            predicates.add(criteriaBuilder.equal(root.get("reportState"), reportState));
        }

        // 應用 WHERE 條件
        if (!predicates.isEmpty()) {
            criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        }

        // 動態排序
        if (sort != null && !sort.isEmpty()) {
            if (dir) {
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get(sort)));
            } else {
                criteriaQuery.orderBy(criteriaBuilder.asc(root.get(sort)));
            }
        }

        // 分頁
        TypedQuery<ReportCase> typedQuery = this.getSession().createQuery(criteriaQuery).setMaxResults(rows);
        if (start != null) {
            typedQuery.setFirstResult(start);
        }

        // 執行查詢
        List<ReportCase> result = typedQuery.getResultList();

        // 返回結果或空值
        if (result != null && !result.isEmpty()) {
            return result;
        } else {
            return null;
        }
    }

}
