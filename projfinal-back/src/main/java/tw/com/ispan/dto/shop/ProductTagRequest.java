package tw.com.ispan.dto.shop;

public class ProductTagRequest {

    private String tagName;

    private String tagDescription;

    public ProductTagRequest() {
    }

    public ProductTagRequest(String tagName, String tagDescription) {
        this.tagName = tagName;
        this.tagDescription = tagDescription;
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

}
