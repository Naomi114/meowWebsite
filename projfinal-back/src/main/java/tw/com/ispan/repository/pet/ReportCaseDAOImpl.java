package tw.com.ispan.repository.pet;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import tw.com.ispan.domain.pet.LostCase;
import tw.com.ispan.domain.pet.ReportCase;

public class ReportCaseDAOImpl implements ReportCaseDAO {

    @PersistenceContext
    private EntityManager entityManager = null;

    public EntityManager getSession() {
        return entityManager;
    }

    @Override
    public Long count(JSONObject param) {
        // 解析传入的 JSON 参数
        Integer speciesId = param.isNull("speciesId") ? null : param.getInt("speciesId");
        Integer breedId = param.isNull("breedId") ? null : param.getInt("breedId");
        Integer furColorId = param.isNull("furColorId") ? null : param.getInt("furColorId");
        Integer cityId = param.isNull("cityId") ? null : param.getInt("cityId");
        Integer distinctAreaId = param.isNull("distinctAreaId") ? null : param.getInt("distinctAreaId");
        String caseTitle = param.isNull("caseTitle") ? null : param.getString("caseTitle");
        Integer caseStateId = param.isNull("caseStateId") ? null : param.getInt("caseStateId");
        Integer reportMemberId = param.isNull("reportMemberId") ? null : param.getInt("reportMemberId"); // 新增字段

        // 构建 Criteria 查询
        CriteriaBuilder criteriaBuilder = this.getSession().getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<LostCase> root = criteriaQuery.from(LostCase.class);

        // 关联 ReportCase 表
        Join<LostCase, ReportCase> reportJoin = root.join("reportCases", JoinType.LEFT);

        // select count(*)
        criteriaQuery.select(criteriaBuilder.count(root));

        // 动态构建 WHERE 条件
        List<Predicate> predicates = new ArrayList<>();

        // 条件：speciesId
        if (speciesId != null) {
            predicates.add(criteriaBuilder.equal(root.get("species").get("speciesId"), speciesId));
        }

        // 条件：breedId
        if (breedId != null) {
            predicates.add(criteriaBuilder.equal(root.get("breed").get("breedId"), breedId));
        }

        // 条件：furColorId
        if (furColorId != null) {
            predicates.add(criteriaBuilder.equal(root.get("furColor").get("furColorId"), furColorId));
        }

        // 条件：cityId
        if (cityId != null) {
            predicates.add(criteriaBuilder.equal(root.get("city").get("cityId"), cityId));
        }

        // 条件：distinctAreaId
        if (distinctAreaId != null) {
            predicates.add(criteriaBuilder.equal(root.get("distinctArea").get("distinctAreaId"), distinctAreaId));
        }

        // 条件：案件标题模糊查询
        if (caseTitle != null && !caseTitle.trim().isEmpty()) {
            String likePattern = "%" + caseTitle + "%";
            predicates.add(criteriaBuilder.like(root.get("caseTitle"), likePattern));
        }

        // 条件：案件状态
        if (caseStateId != null) {
            predicates.add(criteriaBuilder.equal(root.get("caseState").get("caseStateId"), caseStateId));
        }

        // 条件：ReportCase 的创建者（MemberID）
        if (reportMemberId != null) {
            predicates.add(criteriaBuilder.equal(reportJoin.get("member").get("memberId"), reportMemberId));
        }

        // 应用所有条件
        if (!predicates.isEmpty()) {
            criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        }

        // 执行查询
        TypedQuery<Long> query = this.getSession().createQuery(criteriaQuery);
        return query.getSingleResult();
    }

}
