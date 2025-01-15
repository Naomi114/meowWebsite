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
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tag")
public class ProductTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tagId;

    @Column(nullable = false)
    private String tagName;

    private String tagDescription;

    // 雙向關係的多對多端，可反向查找商品
    @ManyToMany(mappedBy = "tags", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("tags")
    private Set<ProductBean> products = new HashSet<>(); // 無序唯一高效查找

    public ProductTag() {
    }

    public ProductTag(Integer tagId, String tagName, String tagDescription, Set<ProductBean> products) {
        this.tagId = tagId;
        this.tagName = tagName;
        this.tagDescription = tagDescription;
        this.products = products;
    }

    @Override
    public String toString() {
        return "TagBean [tagId=" + tagId + ", tagName=" + tagName + ", tagDescription=" + tagDescription + ", products="
                + products + "]";
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagDescription() {
        return tagDescription;
    }

    public void setTagDescription(String tagDescription) {
        this.tagDescription = tagDescription;
    }

    public Set<ProductBean> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductBean> products) {
        this.products = products;
    }

}
