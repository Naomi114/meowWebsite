package tw.com.ispan.controller.shop;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tw.com.ispan.domain.admin.Admin;
import tw.com.ispan.domain.admin.Member;
import tw.com.ispan.domain.shop.Notification;
import tw.com.ispan.repository.admin.AdminRepository;
import tw.com.ispan.repository.admin.MemberRepository;
import tw.com.ispan.service.shop.NotificationService;

@RestController
@RequestMapping("/notifications")
@CrossOrigin
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AdminRepository adminRepository;

    @GetMapping("/member/{memberId}/unread")
    public ResponseEntity<List<Notification>> getUnreadNotificationsForMember(@PathVariable Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("會員不存在"));
        return ResponseEntity.ok(notificationService.getUnreadNotificationsForMember(member));
    }

    @GetMapping("/admin/{adminId}/unread")
    public ResponseEntity<List<Notification>> getUnreadNotificationsForAdmin(@PathVariable Integer adminId) {
        Admin admin = adminRepository.findById(adminId).orElseThrow(() -> new RuntimeException("管理員不存在"));
        return ResponseEntity.ok(notificationService.getUnreadNotificationsForAdmin(admin));
    }

    @PostMapping("/{notificationId}/read")
    public ResponseEntity<String> markNotificationAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok("通知已標記為已讀");
    }
}
