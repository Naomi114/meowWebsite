package tw.com.ispan.domain.shop;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "productCategory")
public class ProductCategoryBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;

    @Column(unique = true)
    private String categoryName;

    private String categoryDescription;

    @Column(nullable = false, length = 10)
    private String defaultUnit;

    @OneToMany(mappedBy = "productCategory", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<ProductBean> products;

    public ProductCategoryBean() {
    }

    public ProductCategoryBean(Integer categoryId, String categoryName, String categoryDescription, String defaultUnit,
            List<ProductBean> products) {
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

    public List<ProductBean> getProducts() {
        return products;
    }

    public void setProducts(List<ProductBean> products) {
        this.products = products;
    }

}
