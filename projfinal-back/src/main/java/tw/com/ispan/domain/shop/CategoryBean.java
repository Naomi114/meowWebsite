package tw.com.ispan.domain.shop;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity(name = "CategoryBean")
@Table(name = "category")
public class CategoryBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;

    @Column(unique = true)
    private String categoryName;

    private String categoryDescription;

    @Column(nullable = false, length = 10)
    private String defaultUnit;

    // 雙向關係的一對多端，可反向查找
    // cascade = CascadeType.remove 刪除類別時，會刪除該類別的所有商品；只有新增、修改、更新同步
    @OneToMany(mappedBy = "category", cascade = { CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH }, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<ProductBean> products = new HashSet<>(); // 無序不重複、高效查找

    public CategoryBean() {
    }

    public CategoryBean(Integer categoryId, String categoryName, String categoryDescription, String defaultUnit,
            Set<ProductBean> products) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
        this.defaultUnit = defaultUnit;
        this.products = products;
    }

    @Override
    public String toString() {
        return "ProductCategoryBean [categoryId=" + categoryId + ", categoryName=" + categoryName
                + ", categoryDescription=" + categoryDescription + ", defaultUnit=" + defaultUnit + ", products="
                + products + "]";
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
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

    public Set<ProductBean> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductBean> products) {
        this.products = products;
    }

}
