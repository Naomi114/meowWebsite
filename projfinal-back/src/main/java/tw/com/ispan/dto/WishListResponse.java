package tw.com.ispan.dto;

import java.util.List;

import tw.com.ispan.domain.shop.WishList;

public class WishListResponse {
    private Boolean success;
    private String message;
    private List<WishList> wishlists; // 願望清單列表
    private Long count;

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

    public List<WishList> getWishlists() {
        return wishlists;
    }

    public void setWishlists(List<WishList> wishlists) {
        this.wishlists = wishlists;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

}
