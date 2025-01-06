package tw.com.ispan.projfinal_back.domain.shop;

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
@Table(name = "tag")
public class TagBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tagId;

    @Column(nullable = false)
    private String tagName;

    private String tagDescription;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductTagBean> productTags;

    public TagBean() {
    }

    public TagBean(Integer tagId, String tagName, String tagDescription, List<ProductTagBean> productTags) {
        this.tagId = tagId;
        this.tagName = tagName;
        this.tagDescription = tagDescription;
        this.productTags = productTags;
    }

    @Override
    public String toString() {
        return "TagBean [tagId=" + tagId + ", tagName=" + tagName + ", tagDescription=" + tagDescription
                + ", productTags=" + productTags + "]";
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

    public List<ProductTagBean> getProductTags() {
        return productTags;
    }

    public void setProductTags(List<ProductTagBean> productTags) {
        this.productTags = productTags;
    }
}

