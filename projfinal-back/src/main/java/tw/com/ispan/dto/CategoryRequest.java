package tw.com.ispan.dto;

import javax.validation.constraints.NotBlank;

public class CategoryRequest {
    private Integer categoryId;

    @NotBlank(message = "類別名稱不能為空")
    private String categoryName;

    private String categoryDescription;

    @NotBlank(message = "新建類別單位不能為空")
    private String unit;

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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

}
