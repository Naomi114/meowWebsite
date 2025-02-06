package tw.com.ispan.repository.shop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tw.com.ispan.domain.shop.CartActionLog;

@Repository
public interface CartActionLogRepository extends JpaRepository<CartActionLog, Long> {
    List<CartActionLog> findByMemberId(Long memberId);
}
