package tw.com.ispan.domain.shop;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tag")
public class TagBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tagId;

    @Column(nullable = false)
    private String tagName;

    private String tagDescription;

    @ManyToMany(mappedBy = "tags", cascade = CascadeType.ALL)
    private List<ProductBean> products;

    public TagBean() {
    }

    public TagBean(Integer tagId, String tagName, String tagDescription, List<ProductBean> products) {
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

    public List<ProductBean> getProducts() {
        return products;
    }

    public void setProducts(List<ProductBean> products) {
        this.products = products;
    }

}
