package tw.com.ispan.projfinal_back.service;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import tw.com.ispan.domain.pet.City;
import tw.com.ispan.domain.pet.LostCase;
import tw.com.ispan.service.pet.LostCaseService;

public class LostCaseServiceTest {

    @Autowired
    private LostCaseService lostCaseService;

    // @Test
    public void testFind() {
        JSONObject obj = new JSONObject()
                // .put("name", "a")
                .put("start", 0)
                .put("rows", 3)
                .put("sort", "id")
                .put("dir", true);
        long count = lostCaseService.count(obj.toString());
        System.out.println("count=" + count);

        List<LostCase> find = lostCaseService.find(obj.toString());
        if (find != null && !find.isEmpty()) {
            for (LostCase bean : find) {
                System.out.println("bean=" + bean);
            }
        }
        System.out.println("----------");
    }

    // @Test
    public void testRemove() {
        Integer[] testIds = { 1000, 200, null };

        for (Integer testId : testIds) {
            boolean existsBefore = lostCaseService.exists(testId);
            System.out.println("Exists before removal (ID=" + testId + "): " + existsBefore);

            boolean removed = lostCaseService.remove(testId);
            System.out.println("Removed (ID=" + testId + "): " + removed);

            boolean existsAfter = lostCaseService.exists(testId);
            System.out.println("Exists after removal (ID=" + testId + "): " + existsAfter);
            System.out.println("----------");
        }
    }

    // @Test
    public void testUpdate() {
        JSONObject obj = new JSONObject()
                .put("lostCaseId", 1000) // 要更新的案件 ID
                .put("caseTitle", "Updated Case Title")
                .put("speciesId", 1) // 物种 ID，例如：狗
                .put("breedId", 3) // 品种 ID
                .put("furColorId", 5) // 毛色 ID
                .put("cityId", 2) // 城市 ID，例如：新北市
                .put("distinctAreaId", 8) // 区域 ID
                .put("street", "New Updated Street Name")
                .put("gender", "Male")
                .put("sterilization", "Yes")
                .put("age", 3)
                .put("microChipNumber", 12345678)
                .put("suspLost", true)
                .put("latitude", 25.0330)
                .put("longitude", 121.5654)
                .put("donationAmount", 1000)
                .put("caseStateId", 5) // 案件状态 ID
                .put("lostExperience", "This is an updated lost experience.")
                .put("contactInformation", "New Contact Info")
                .put("featureDescription", "Updated feature description.")
                .put("caseUrl", "http://updatedcaseurl.com");

        LostCase modify = lostCaseService.modify(obj.toString());
        if (modify != null) {
            System.out.println("修改成功: " + modify);
        } else {
            System.out.println("修改失败，检查输入数据或数据库状态！");
        }
        System.out.println("----------");
    }

    // @Test
    public void testCreate() {
        JSONObject obj = new JSONObject()
                .put("caseTitle", "New Lost Case")
                .put("speciesId", 1) // 物种 ID，例如：狗
                .put("breedId", 3) // 品种 ID
                .put("furColorId", 5) // 毛色 ID
                .put("cityId", 2) // 城市 ID，例如：新北市
                .put("distinctAreaId", 8) // 区域 ID
                .put("street", "New Street Name")
                .put("gender", "Female")
                .put("sterilization", "No")
                .put("age", 2)
                .put("microChipNumber", 98765432)
                .put("suspLost", false)
                .put("latitude", 25.0335)
                .put("longitude", 121.5651)
                .put("donationAmount", 500)
                .put("caseStateId", 5) // 案件状态 ID
                .put("lostExperience", "This is a test lost experience.")
                .put("contactInformation", "Test Contact Info")
                .put("featureDescription", "Test feature description.")
                .put("caseUrl", "http://testcaseurl.com");

        LostCase create = lostCaseService.create(obj.toString());
        if (create != null) {
            System.out.println("创建成功: " + create);
        } else {
            System.out.println("创建失败，请检查输入数据或数据库配置！");
        }
        System.out.println("----------");
    }

    // @Test
    public void testExists() {
        Integer[] testIds = { 2, 200, null };

        for (Integer testId : testIds) {
            boolean exists = lostCaseService.exists(testId);
            System.out.println("Test ID: " + testId + " -> Exists: " + exists);
        }

        System.out.println("----------");
    }

    // @Test
    public void testFindById() {
        Integer[] testIds = { 2, 200, null };

        for (Integer testId : testIds) {
            LostCase foundCase = lostCaseService.findById(testId);
            if (foundCase != null) {
                System.out.println("Found LostCase with ID " + testId + ": " + foundCase);
            } else {
                System.out.println("No LostCase found with ID " + testId);
            }
        }
        System.out.println("----------");
    }

    // @Test
    public void testSelect() {
        // 测试条件一：不带任何条件
        List<LostCase> selects1 = lostCaseService.select(null);
        System.err.println("LostCases (no filter): " + (selects1 != null ? selects1 : "No results"));

        // 测试条件二：根据 ID 查询
        LostCase bean = new LostCase();
        bean.setLostCaseId(1);
        List<LostCase> selects2 = lostCaseService.select(bean);
        System.err.println("LostCases (filtered by ID=1): " + (selects2 != null ? selects2 : "No results"));

        // 测试条件三：根据多个字段查询（示例）
        LostCase bean2 = new LostCase();
        bean2.setCaseTitle("Example Title"); // 模糊查询条件
        bean2.setCity(new City("New Taipei City")); // 城市查询条件
        List<LostCase> selects3 = lostCaseService.select(bean2);
        System.err.println("LostCases (filtered by title and city): " + (selects3 != null ? selects3 : "No results"));
        System.out.println("----------");
    }

}
