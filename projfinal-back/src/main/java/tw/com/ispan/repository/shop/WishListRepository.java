package tw.com.ispan.repository.shop;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.com.ispan.domain.shop.WishListBean;

public interface WishListRepository extends JpaRepository<WishListBean, Integer> {

}
