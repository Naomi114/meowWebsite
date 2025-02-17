package tw.com.ispan.dto.shop;

import tw.com.ispan.domain.shop.ProductTag;

public class TagDTO {
    private Integer tagId;
    private String tagName;

    public TagDTO(ProductTag tag) {
        this.tagId = tag.getTagId();
        this.tagName = tag.getTagName();
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

}
