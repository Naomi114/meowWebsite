package tw.com.ispan.dto;

import javax.validation.constraints.NotBlank;

/*  ProductImageRequest 的責任
    1. 負責單張圖片的數據傳遞。
    2. 圖片數據驗證（如是否為主圖片、URL 是否有效）。
    3. 不直接涉及業務邏輯，只用於接收和傳遞圖片數據。
*/

public class ProductImageRequest {

    @NotBlank(message = "圖片 URL 不能為空")
    private String imageUrl;

    private Boolean isPrimary;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }
}
