package tw.com.ispan.repository.shop;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import tw.com.ispan.domain.admin.Member;
import tw.com.ispan.domain.shop.Product;
import tw.com.ispan.domain.shop.WishList;

public interface WishListRepository
        extends JpaRepository<WishList, Integer>, JpaSpecificationExecutor<WishList> {

    boolean existsByMemberAndProduct(Member member, Product product);

    // 移除收藏
    Optional<WishList> findByMemberAndProduct(Member member, Product product);

    // 查詢收藏
    List<WishList> findByMember(Member member);

    // 刪除商品時，確認受到影響的會員
    @Query("SELECT DISTINCT w.member FROM WishList w WHERE w.product = :product")
    List<Member> findMembersByProduct(@Param("product") Product product);

    // 刪除商品時，刪除關聯願望清單內容；對應實體的資料表名稱
    // @OneToMany 刪除商品時，解除@Table表格關聯
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Wishlist WHERE FK_productId = :productId", nativeQuery = true)
    void removeWishListByProductId(@Param("productId") Integer productId);
}
