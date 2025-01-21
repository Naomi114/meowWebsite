package tw.com.ispan.init;

/*
    POJO類別: Plain Old Java Object
    本身沒特別用處，單純提供更靈活的數據結構來使用資料庫
*/ 
public class CategoryInitData {
    private String categoryName;
    private String defaultUnit;
    private String categoryDescription;

    public CategoryInitData(String categoryName, String defaultUnit, String categoryDescription) {
        this.categoryName = categoryName;
        this.defaultUnit = defaultUnit;
        this.categoryDescription = categoryDescription;
    }

    // Getters 和 Setters
    public String getCategoryName() {
        return categoryName;
    }

    public String getDefaultUnit() {
        return defaultUnit;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }
}
    
