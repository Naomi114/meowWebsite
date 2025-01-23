package tw.com.ispan.specification;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import tw.com.ispan.domain.pet.RescueCase;
import tw.com.ispan.domain.pet.forRescue.RescueProgress;
import tw.com.ispan.dto.pet.RescueSearchCriteria;

//此工具類為生成救援案件頁面的查詢條件
public class RescueCaseSpecification {
	
	// 此方法目的為返回一個Specification<RescueCase>查詢條件給JpaSpecificationExecutor介面方法使用
	// 參數為使用者從前端輸入條件被controller中RescueSearchCriteria接收，丟進來動態生成對應sql語句
	public static Specification<RescueCase> withRescueSearchCriteria(RescueSearchCriteria criteria) {
		// withRescueSearchCriteria方法直接return由lambda表達式生成的Specification<RescueCase>物件 {
		// }內為override toPredicate()的方法體，可返回predicate
		// 利用CriteriaBuilder底下方法可組裝並返回predicate物件
		return (root, query, cb) -> {
			Predicate predicate = cb.conjunction(); // cb.conjunction()可生成初始條件，表示 SQL 中的 where 1=1，最後返回此物件出去

			// 關鍵字模糊查詢 (匹配RescueCase所有字串屬性，包含特徵tag)
			if (criteria.getKeyword() != null && !criteria.getKeyword().isEmpty()) {
				// 一對多的關聯，需另外先建立 Join 從 RescueCase 到 RescueProgress (會自動找尋對應id的資料)，改從此為基底出發作查詢
				// SELECT rc.* FROM RescueCase rc JOIN RescueProgress rp ON rc.id = rp.rescueCaseId WHERE rp.content LIKE '%keyword%';
				Join<RescueCase, RescueProgress> rescueProgressJoin = root.join("rescueProgresses");

				// cb.or()組裝WHERE條件 (caseTitle LIKE '%<keyword>%' OR species LIKE '%<keyword>%' OR...)
				// 注意:cb.like()內參數只能放String，因此遇到非字串屬性要先轉換
				
				//要加到語句的查詢的模糊字串，用.trim()將使用者輸入的空白字串刪除，確保查詢結果正確性
				String fuzzyKeyword =  "%" + criteria.getKeyword().trim() + "%";
				System.out.println("//"+fuzzyKeyword+"//");
				
				//當 root.get("furColor") 為 null 時，SQL 查詢會生成類似 NULL LIKE '%哈哈%' 的條件，這永遠不會匹配任何資料。對於可能為空的欄位，應該在查詢中進行 null 檢查。
				//root.get("species").get("species") 等操作確實等同於自動 JOIN，但如果某些關聯屬性本身為 null，則查詢也可能失敗，因為 NULL 無法匹配
				//像 caseTitle、gender 等直接屬性可以保留原有的 LIKE 條件，因為它們不涉及關聯
				Predicate keywordPredicate = 
						cb.or(
						cb.like(root.get("caseTitle"), fuzzyKeyword),
						cb.like(root.get("species").get("species"), fuzzyKeyword), // 寫species會返回對應的Species實體，等同自動JOINspecies表但仍需再找到需查詢的欄位.get("species")																				
						cb.and(
								cb.isNotNull(root.get("breed")),
								cb.like(root.get("breed").get("breed"), fuzzyKeyword)
								),
						cb.and(
								cb.isNotNull(root.get("furColor")),
								cb.like(root.get("furColor").get("furColor"), fuzzyKeyword)
								),
						cb.like(root.get("gender"), fuzzyKeyword),
						cb.like(root.get("sterilization"), fuzzyKeyword),
						cb.like(root.get("city").get("city"), fuzzyKeyword),
						cb.like(root.get("distinctArea").get("distinctAreaName"), fuzzyKeyword),
						cb.like(root.get("street"), fuzzyKeyword),
						cb.like(root.get("tag"), fuzzyKeyword),
						cb.like(root.get("rescueReason"), fuzzyKeyword),
						cb.like(rescueProgressJoin.get("progressDetail"), fuzzyKeyword) // 改用rescueProgressJoin
						);
				System.out.println(444444);
//				query.distinct(true); // 消除可能因@onetomany又去join導致查詢出相同多筆case，以此可消除重複結果
				predicate = cb.and(predicate, keywordPredicate); // 將不同查詢條件用AND組合 等同WHERE 條件A AND 條件B...
			}

			
			//以下為精準查詢
			// 救援狀態
			if (criteria.getCaseStateId() != null) {
				predicate = cb.and(predicate, cb.equal(root.get("caseState").get("caseStateId"), criteria.getCaseStateId()));
			}

			// 縣市
			if (criteria.getCityId() != null) {
				predicate = cb.and(predicate, cb.equal(root.get("city").get("cityId"), criteria.getCityId()));
			}

			// 鄉鎮區
			if (criteria.getDistrictAreaId() != null) {
				predicate = cb.and(predicate, cb.equal(root.get("distinctArea").get("distinctAreaId"), criteria.getDistrictAreaId()));
			}

			// 物種
			if (criteria.getSpeciesId() != null) {
				predicate = cb.and(predicate, cb.equal(root.get("species").get("speciesId"), criteria.getSpeciesId()));
			}

			// 品種
			if (criteria.getBreedId() != null) {
				predicate = cb.and(predicate, cb.equal(root.get("breed").get("breedId"), criteria.getBreedId()));
			}
			
			//毛色(另外還有自訂義，要變成用字串查)
			if (criteria.getFurColorId() != null) {
				predicate = cb.and(predicate, cb.equal(root.get("furColor").get("furColorId"), criteria.getFurColorId()));
			}

			// 走失標記
			if (criteria.getSuspectLost() != null) {
				predicate = cb.and(predicate, cb.equal(root.get("suspLost"), criteria.getSuspectLost()));
			}

			return predicate;
		};
	}
}
