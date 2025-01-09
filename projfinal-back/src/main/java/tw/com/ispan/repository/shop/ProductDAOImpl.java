package tw.com.ispan.repository.shop;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import tw.com.ispan.domain.shop.ProductBean;
import tw.com.ispan.util.DatetimeConverter;

@Repository
public class ProductDAOImpl implements ProductDAO {
	@PersistenceContext
	private EntityManager entityManager;

	public EntityManager getSession() {
		return entityManager;
	}

	@Override
	public Long count(JSONObject param) {
		Integer productId = param.isNull("productId") ? null : param.getInt("productId");
		String productName = param.isNull("productName") ? null : param.getString("productName");
		Double priceMin = param.isNull("priceMin") ? null : param.getDouble("priceMin");
		Double priceMax = param.isNull("priceMax") ? null : param.getDouble("priceMax");
		String createdMin = param.isNull("createdMin") ? null : param.getString("createdMin");
		String createdMax = param.isNull("createdMax") ? null : param.getString("createdMax");

		CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<ProductBean> root = criteriaQuery.from(ProductBean.class);

		criteriaQuery.select(criteriaBuilder.count(root));

		List<Predicate> predicates = new ArrayList<>();

		if (productId != null) {
			predicates.add(criteriaBuilder.equal(root.get("productId"), productId));
		}

		if (productName != null && !productName.isEmpty()) {
			predicates.add(criteriaBuilder.like(root.get("productName"), "%" + productName + "%"));
		}

		if (priceMin != null) {
			predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("salePrice"), priceMin));
		}

		if (priceMax != null) {
			predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("salePrice"), priceMax));
		}

		if (createdMin != null) {
			predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"),
					DatetimeConverter.parse(createdMin, "yyyy-MM-dd HH:mm:ss")));
		}

		if (createdMax != null) {
			predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"),
					DatetimeConverter.parse(createdMax, "yyyy-MM-dd HH:mm:ss")));
		}

		if (!predicates.isEmpty()) {
			criteriaQuery.where(predicates.toArray(new Predicate[0]));
		}

		TypedQuery<Long> query = getSession().createQuery(criteriaQuery);
		return query.getSingleResult();
	}

	@Override
	public List<ProductBean> find(JSONObject param) {
		Integer productId = param.isNull("productId") ? null : param.getInt("productId");
		String productName = param.isNull("productName") ? null : param.getString("productName");
		Double priceMin = param.isNull("priceMin") ? null : param.getDouble("priceMin");
		Double priceMax = param.isNull("priceMax") ? null : param.getDouble("priceMax");
		String createdMin = param.isNull("createdMin") ? null : param.getString("createdMin");
		String createdMax = param.isNull("createdMax") ? null : param.getString("createdMax");
		Integer start = param.isNull("start") ? null : param.getInt("start");
		Integer rows = param.isNull("rows") ? 5 : param.getInt("rows");
		String sort = param.isNull("sort") ? null : param.getString("sort");
		Boolean dir = param.isNull("dir") ? true : param.getBoolean("dir");

		CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
		CriteriaQuery<ProductBean> criteriaQuery = criteriaBuilder.createQuery(ProductBean.class);
		Root<ProductBean> root = criteriaQuery.from(ProductBean.class);

		List<Predicate> predicates = new ArrayList<>();

		if (productId != null) {
			predicates.add(criteriaBuilder.equal(root.get("productId"), productId));
		}

		if (productName != null && !productName.isEmpty()) {
			predicates.add(criteriaBuilder.like(root.get("productName"), "%" + productName + "%"));
		}

		if (priceMin != null) {
			predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("salePrice"), priceMin));
		}

		if (priceMax != null) {
			predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("salePrice"), priceMax));
		}

		if (createdMin != null) {
			predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"),
					DatetimeConverter.parse(createdMin, "yyyy-MM-dd HH:mm:ss")));
		}

		if (createdMax != null) {
			predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"),
					DatetimeConverter.parse(createdMax, "yyyy-MM-dd HH:mm:ss")));
		}

		if (!predicates.isEmpty()) {
			criteriaQuery.where(predicates.toArray(new Predicate[0]));
		}

		if (sort != null && !sort.isEmpty()) {
			if (dir) {
				criteriaQuery.orderBy(criteriaBuilder.asc(root.get(sort)));
			} else {
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get(sort)));
			}
		}

		TypedQuery<ProductBean> query = getSession().createQuery(criteriaQuery).setMaxResults(rows);

		if (start != null) {
			query.setFirstResult(start);
		}

		return query.getResultList();
	}
}
