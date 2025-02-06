package tw.com.ispan.repository.shop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tw.com.ispan.domain.admin.Admin;
import tw.com.ispan.domain.admin.Member;
import tw.com.ispan.domain.shop.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    // 查詢會員未讀通知
    List<Notification> findByMemberAndReadStatusFalse(Member member);
    
    // 查詢管理員未讀通知
    List<Notification> findByAdminAndReadStatusFalse(Admin admin);
}

