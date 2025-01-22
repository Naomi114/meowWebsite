package tw.com.ispan.dto;

import javax.validation.constraints.NotBlank;

// import tw.com.ispan.domain.admin.Member;
// import tw.com.ispan.domain.shop.Product;

public class WishListRequest {

    @NotBlank(message = "會員編號不能為空")
    private Integer memberId;

    @NotBlank(message = "商品名稱不能為空")
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

    // @NotBlank(message = "會員編號不能為空")
    // private Member member;

    // @NotBlank(message = "商品名稱不能為空")
    // private Product product;

    // public Product getProduct() {
    // return product;
    // }

    // public void setProduct(Product product) {
    // this.product = product;
    // }

    // public Member getMember() {
    // return member;
    // }

    // public void setMember(Member member) {
    // this.member = member;
    // }

    // // 輔助方法：getMember => getMemberId
    // public Integer getMemberId() {
    // if (this.member == null) {
    // throw new IllegalArgumentException("會員資料為空");
    // }
    // return this.member.getMemberId();
    // }

    // // 輔助方法：getProduct => getProductId
    // public Integer getProductId() {
    // if (this.product == null) {
    // throw new IllegalArgumentException("商品資料為空");
    // }
    // return this.product.getProductId();
    // }

}
