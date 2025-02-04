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

    boolean existsByMemberAndProduct(Member member, Product product);

    // 移除收藏
    Optional<WishList> findByMemberAndProduct(Member member, Product product);

    // 查詢收藏
    List<WishList> findByMember(Member member);

}
