package tw.com.ispan.dto;

import javax.validation.constraints.NotBlank;

public class CategoryRequest {
    private Integer categoryId;

    @NotBlank(message = "類別名稱不能為空")
    private String categoryName;

    private String categoryDescription;

    @NotBlank(message = "新建類別單位不能為空")
    private String defaultUnit;

    public CategoryRequest() {
    }

    public CategoryRequest(Integer categoryId, @NotBlank(message = "類別名稱不能為空") String categoryName,
            String categoryDescription, @NotBlank(message = "新建類別單位不能為空") String defaultUnit) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
        this.defaultUnit = defaultUnit;
    }

    // 已上架的商品，修改類別
    public CategoryRequest(Integer resolvedCategoryId) {
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public String getDefaultUnit() {
        return defaultUnit;
    }

    public void setDefaultUnit(String defaultUnit) {
        this.defaultUnit = defaultUnit;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

}
