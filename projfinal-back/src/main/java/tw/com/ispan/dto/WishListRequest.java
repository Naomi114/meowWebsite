package tw.com.ispan.dto;

import javax.validation.constraints.NotNull;

public class WishListRequest {

    @NotNull(message = "會員編號不能為空")
    private Integer memberId;

    @NotNull(message = "商品編號不能為空")
    private Integer productId;

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }
}
