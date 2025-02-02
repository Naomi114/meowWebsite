package tw.com.ispan.domain.shop;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import tw.com.ispan.domain.admin.Admin;
import tw.com.ispan.domain.admin.Member;

@Entity
@Table(name = "Notification")
public class Notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;  // 通知標題
    private String message;  // 通知內容
    private LocalDateTime createdAt;  // 通知時間

    @ManyToOne
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_notification_member"))
    private Member member;  // 若是發給會員，關聯會員

    @ManyToOne
    @JoinColumn(name = "admin_id", foreignKey = @ForeignKey(name = "fk_notification_admin"))
    private Admin admin;  // 若是發給管理員，關聯管理員

    private Boolean readStatus = false; // 通知是否已讀

    public Notification() {
    }

    public Notification(Long id, String title, String message, LocalDateTime createdAt, Member member, Admin admin,
            Boolean readStatus) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.createdAt = createdAt;
        this.member = member;
        this.admin = admin;
        this.readStatus = readStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public Boolean getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(Boolean readStatus) {
        this.readStatus = readStatus;
    }

    @Override
    public String toString() {
        return "Notification [id=" + id + ", title=" + title + ", message=" + message + ", createdAt=" + createdAt
                + ", member=" + member + ", admin=" + admin + ", readStatus=" + readStatus + "]";
    }

    
}

