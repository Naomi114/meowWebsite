package tw.com.ispan.dto.shop;

import java.util.List;

public class CategoryResponse {
    private Boolean success;
    private String message;
    private List<CategoryDTO> categories;

    public CategoryResponse() {
    }

    public CategoryResponse(Boolean success, String message, List<CategoryDTO> categories) {
        this.success = success;
        this.message = message;
        this.categories = categories;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryDTO> categories) {
        this.categories = categories;
    }

}
