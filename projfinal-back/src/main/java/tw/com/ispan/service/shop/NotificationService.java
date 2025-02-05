package tw.com.ispan.service.shop;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.com.ispan.domain.admin.Admin;
import tw.com.ispan.domain.admin.Member;
import tw.com.ispan.domain.shop.Notification;
import tw.com.ispan.repository.admin.AdminRepository;
import tw.com.ispan.repository.shop.NotificationRepository;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private AdminRepository adminRepository;

    // 發送通知給會員
    public void notifyMember(Member member, String title, String message) {
        Notification notification = new Notification();
        notification.setMember(member);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(notification);
    }

    // 發送通知給管理員
    public void notifyAdmin(String title, String message) {
        List<Admin> admins = adminRepository.findAll();
        for (Admin admin : admins) {
            Notification notification = new Notification();
            notification.setAdmin(admin);
            notification.setTitle(title);
            notification.setMessage(message);
            notification.setCreatedAt(LocalDateTime.now());
            notificationRepository.save(notification);
        }
    }

    // 會員查詢未讀通知
    public List<Notification> getUnreadNotificationsForMember(Member member) {
        return notificationRepository.findByMemberAndReadStatusFalse(member);
    }

    // 管理員查詢未讀通知
    public List<Notification> getUnreadNotificationsForAdmin(Admin admin) {
        return notificationRepository.findByAdminAndReadStatusFalse(admin);
    }

    // 設定通知為已讀
    public void markAsRead(Long notificationId) {
        Optional<Notification> notificationOpt = notificationRepository.findById(notificationId);
        if (notificationOpt.isPresent()) {
            Notification notification = notificationOpt.get();
            notification.setReadStatus(true);
            notificationRepository.save(notification);
        }
    }
}
