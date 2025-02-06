package tw.com.ispan.service.shop;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.com.ispan.domain.shop.CartActionLog;
import tw.com.ispan.repository.shop.CartActionLogRepository;

@Service
public class CartActionLogService {
    @Autowired
    private CartActionLogRepository cartActionLogRepository;

    public void saveLog(CartActionLog log) {
        cartActionLogRepository.save(log);
    }

    public List<CartActionLog> getLogsByMemberId(Long memberId) {
        return cartActionLogRepository.findByMemberId(memberId);
    }
}
