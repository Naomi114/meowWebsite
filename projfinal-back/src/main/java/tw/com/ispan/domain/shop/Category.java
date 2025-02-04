package tw.com.ispan.domain.shop;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;

    @Column(unique = true)
    private String categoryName;

    @Lob
    private String categoryDescription;

    @Column(length = 10)
    private String defaultUnit;

    // 雙向關係的一對多端，可反向查找
    // cascade = CascadeType.remove 刪除類別時，會刪除該類別的所有商品；只有新增、修改、更新同步
    @OneToMany(mappedBy = "category", cascade = { CascadeType.PERSIST,
            CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JsonManagedReference("category")
    private Set<Product> products = new HashSet<>(); // 無序不重複、高效查找

    public Category() {
    }

    public Category(Integer categoryId, String categoryName, String categoryDescription, String defaultUnit,
            Set<Product> products) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
        this.defaultUnit = defaultUnit;
        this.products = products;
    }

    // 接收參數 CategoryServiceIntegrationTest
    public Category(String categoryName, String defaultUnit) {
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

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

}
