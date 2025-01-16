package tw.com.ispan.repository.shop;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import tw.com.ispan.domain.shop.WishList;

public interface WishListRepository
                extends JpaRepository<WishList, Integer>, JpaSpecificationExecutor<WishList> {

}
