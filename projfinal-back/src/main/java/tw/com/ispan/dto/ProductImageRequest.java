package tw.com.ispan.dto;

import javax.validation.constraints.NotBlank;

/*  ProductImageRequest 的責任
    1. 負責單張圖片的數據傳遞
    2. 圖片數據驗證（如是否為主圖片）
    3. 不直接涉及業務邏輯，只用於接收和傳遞圖片數據
    4. imageUrl 欄位由 ProductImageService 自動生成
*/

public class ProductImageRequest {

    @NotBlank(message = "檔案名稱不能為空")
    private String filename;

    // 前端提供主圖勾選
    private Boolean isPrimary;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }
}
