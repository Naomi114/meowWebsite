package tw.com.ispan.repository.shop;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import tw.com.ispan.domain.admin.Member;
import tw.com.ispan.domain.shop.Product;
import tw.com.ispan.domain.shop.WishList;

public interface WishListRepository
        extends JpaRepository<WishList, Integer>, JpaSpecificationExecutor<WishList> {

    boolean existsByMemberAndProduct(Member memberId, Product product);

    // 精確查詢
    Optional<WishList> findByMemberAndProduct(Member memberId, Product product);

    // 模糊查詢
    // 根據 Member 和 Product 的 productName 屬性進行模糊查詢
    List<WishList> findByMemberAndProductProductNameContaining(Member member, String productName);

}
