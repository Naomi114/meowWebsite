// package tw.com.ispan.projfinal_back.service;

// import java.util.List;

// import org.json.JSONObject;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;

// import tw.com.ispan.domain.pet.City;
// import tw.com.ispan.domain.pet.LostCase;
// import tw.com.ispan.service.pet.ReportCaseService;

// public class ReportCaseServiceTest {

// @Autowired
// private ReportCaseService reportCaseService;

// @Test
// public void testFind() {
// JSONObject obj = new JSONObject()
// // .put("name", "a")
// .put("start", 0)
// .put("rows", 3)
// .put("sort", "id")
// .put("dir", true);
// long count = reportCaseService.count(obj.toString());
// System.out.println("count=" + count);

// List<LostCase> find = reportCaseService.find(obj.toString());
// if (find != null && !find.isEmpty()) {
// for (LostCase bean : find) {
// System.out.println("bean=" + bean);
// }
// }
// System.out.println("----------");
// }

// // @Test
// public void testExists() {
// Integer[] testIds = { 2, 200, null };

// for (Integer testId : testIds) {
// boolean exists = reportCaseService.exists(testId);
// System.out.println("Test ID: " + testId + " -> Exists: " + exists);
// }

// System.out.println("----------");
// }

// // @Test
// public void testFindById() {
// Integer[] testIds = { 2, 200, null };

// for (Integer testId : testIds) {
// LostCase foundCase = reportCaseService.findById(testId);
// if (foundCase != null) {
// System.out.println("Found LostCase with ID " + testId + ": " + foundCase);
// } else {
// System.out.println("No LostCase found with ID " + testId);
// }
// }
// System.out.println("----------");
// }

// // @Test
// public void testRemove() {
// Integer[] testIds = { 1000, 200, null };

// for (Integer testId : testIds) {
// boolean existsBefore = reportCaseService.exists(testId);
// System.out.println("Exists before removal (ID=" + testId + "): " +
// existsBefore);

// boolean removed = reportCaseService.remove(testId);
// System.out.println("Removed (ID=" + testId + "): " + removed);

// boolean existsAfter = reportCaseService.exists(testId);
// System.out.println("Exists after removal (ID=" + testId + "): " +
// existsAfter);
// System.out.println("----------");
// }
// }

// // @Test
// public void testSelect() {
// // 测试条件一：不带任何条件
// List<LostCase> selects1 = reportCaseService.select(null);
// System.err.println("LostCases (no filter): " + (selects1 != null ? selects1 :
// "No results"));

// // 测试条件二：根据 ID 查询
// LostCase bean = new LostCase();
// bean.setLostCaseId(1);
// List<LostCase> selects2 = reportCaseService.select(bean);
// System.err.println("LostCases (filtered by ID=1): " + (selects2 != null ?
// selects2 : "No results"));

// // 测试条件三：根据多个字段查询（示例）
// LostCase bean2 = new LostCase();
// bean2.setCaseTitle("Example Title"); // 模糊查询条件
// bean2.setCity(new City("New Taipei City")); // 城市查询条件
// List<LostCase> selects3 = reportCaseService.select(bean2);
// System.err.println("LostCases (filtered by title and city): " + (selects3 !=
// null ? selects3 : "No results"));
// System.out.println("----------");
// }
// }
