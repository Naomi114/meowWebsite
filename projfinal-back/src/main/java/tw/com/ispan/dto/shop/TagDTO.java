package tw.com.ispan.dto.shop;

import tw.com.ispan.domain.shop.ProductTag;

// API 回應會包含 tags 陣列，確保前端可以正確篩選
// 如果 DTO 只用來回傳前端，使用 getter，但不加 setter
public class TagDTO {
    private Integer tagId;
    private String tagName;

    public TagDTO(ProductTag tag) {
        this.tagId = tag.getTagId();
        this.tagName = tag.getTagName();
    }

    public TagDTO() {
    }

    public Integer getTagId() {
        return tagId;
    }

    public String getTagName() {
        return tagName;
    }

}

