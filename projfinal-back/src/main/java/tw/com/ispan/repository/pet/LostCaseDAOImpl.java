package tw.com.ispan.repository.pet;

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
import tw.com.ispan.domain.pet.LostCase;

public class LostCaseDAOImpl implements LostCaseDAO {

    @PersistenceContext
    private EntityManager entityManager = null;

    public EntityManager getSession() {
        return entityManager;
    }

    @Override
    public Long count(JSONObject param) {
        // 解析传入的 JSON 参数
        Integer memberId = param.isNull("memberId") ? null : param.getInt("memberId");
        Integer speciesId = param.isNull("speciesId") ? null : param.getInt("speciesId");
        Integer breedId = param.isNull("breedId") ? null : param.getInt("breedId");
        Integer furColorId = param.isNull("furColorId") ? null : param.getInt("furColorId");
        Integer cityId = param.isNull("cityId") ? null : param.getInt("cityId");
        Integer distinctAreaId = param.isNull("distinctAreaId") ? null : param.getInt("distinctAreaId");
        String caseTitle = param.isNull("caseTitle") ? null : param.getString("caseTitle");
        Integer caseStateId = param.isNull("caseStateId") ? null : param.getInt("caseStateId");

        // 构建 Criteria 查询
        CriteriaBuilder criteriaBuilder = this.getSession().getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<LostCase> root = criteriaQuery.from(LostCase.class);

        // select count(*)
        criteriaQuery.select(criteriaBuilder.count(root));

        // 动态构建 WHERE 条件
        List<Predicate> predicates = new ArrayList<>();

        // 条件：memberId
        if (memberId != null) {
            predicates.add(criteriaBuilder.equal(root.get("member").get("memberId"), memberId));
        }

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

        // 应用所有条件
        if (!predicates.isEmpty()) {
            criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        }

        // 执行查询
        TypedQuery<Long> query = this.getSession().createQuery(criteriaQuery);
        return query.getSingleResult();
    }

    @Override
    public List<LostCase> find(JSONObject param) {
        System.out.println("param=" + param);

        // 动态查询参数解析
        Integer speciesId = param.isNull("speciesId") ? null : param.getInt("speciesId");
        Integer breedId = param.isNull("breedId") ? null : param.getInt("breedId");
        Integer furColorId = param.isNull("furColorId") ? null : param.getInt("furColorId");
        Integer cityId = param.isNull("cityId") ? null : param.getInt("cityId");
        Integer distinctAreaId = param.isNull("distinctAreaId") ? null : param.getInt("distinctAreaId");
        String caseTitle = param.isNull("caseTitle") ? null : param.getString("caseTitle");
        Integer caseStateId = param.isNull("caseStateId") ? null : param.getInt("caseStateId");
        Integer memberId = param.isNull("memberId") ? null : param.getInt("memberId");

        Integer start = param.isNull("start") ? null : param.getInt("start"); // 分页开始索引
        Integer rows = param.isNull("rows") ? 5 : param.getInt("rows"); // 分页大小
        String sort = param.isNull("sort") ? null : param.getString("sort"); // 排序字段
        boolean dir = param.isNull("dir") ? false : param.getBoolean("dir"); // 排序方向：false=asc，true=desc

        // 构建 Criteria 查询
        CriteriaBuilder criteriaBuilder = this.getSession().getCriteriaBuilder();
        CriteriaQuery<LostCase> criteriaQuery = criteriaBuilder.createQuery(LostCase.class);
        Root<LostCase> root = criteriaQuery.from(LostCase.class);

        // 动态构建 WHERE 条件
        List<Predicate> predicates = new ArrayList<>();

        if (speciesId != null) {
            predicates.add(criteriaBuilder.equal(root.get("species").get("speciesId"), speciesId));
        }

        if (breedId != null) {
            predicates.add(criteriaBuilder.equal(root.get("breed").get("breedId"), breedId));
        }

        if (furColorId != null) {
            predicates.add(criteriaBuilder.equal(root.get("furColor").get("furColorId"), furColorId));
        }

        if (cityId != null) {
            predicates.add(criteriaBuilder.equal(root.get("city").get("cityId"), cityId));
        }

        if (distinctAreaId != null) {
            predicates.add(criteriaBuilder.equal(root.get("distinctArea").get("distinctAreaId"), distinctAreaId));
        }

        if (caseTitle != null && !caseTitle.trim().isEmpty()) {
            String likePattern = "%" + caseTitle + "%";
            predicates.add(criteriaBuilder.like(root.get("caseTitle"), likePattern));
        }

        if (caseStateId != null) {
            predicates.add(criteriaBuilder.equal(root.get("caseState").get("caseStateId"), caseStateId));
        }

        if (memberId != null) {
            predicates.add(criteriaBuilder.equal(root.get("member").get("memberId"), memberId));
        }

        // 应用 WHERE 条件
        if (!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
        }

        // 动态排序
        if (sort != null && !sort.isEmpty()) {
            if (dir) {
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get(sort)));
            } else {
                criteriaQuery.orderBy(criteriaBuilder.asc(root.get(sort)));
            }
        }

        // 分页
        TypedQuery<LostCase> typedQuery = this.getSession().createQuery(criteriaQuery)
                .setMaxResults(rows);
        if (start != null) {
            typedQuery = typedQuery.setFirstResult(start);
        }

        // 执行查询
        List<LostCase> result = typedQuery.getResultList();

        // 返回结果或空值
        if (result != null && !result.isEmpty()) {
            return result;
        } else {
            return null;
        }
    }

}
