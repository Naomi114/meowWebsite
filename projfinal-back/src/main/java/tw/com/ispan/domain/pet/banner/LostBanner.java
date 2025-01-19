package tw.com.ispan.domain.pet.banner;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import tw.com.ispan.domain.pet.LostCase;

@Entity
@Table(name = "LostBanner")
public class LostBanner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增流水號
    @Column(name = "bannerId")
    private Integer bannerId;

    @Column(name = "OnlineDate", nullable = false)
    private LocalDateTime onlineDate;

    @Column(name = "DueDate", nullable = false)
    private LocalDateTime dueDate;

    // 與 LostCase 的一對一關聯
    @OneToOne
    @JoinColumn(name = "lostCaseId", referencedColumnName = "lostCaseId")
    private LostCase lostCase;

    // Getters and Setters
    public Integer getBannerId() {
        return bannerId;
    }

    public void setBannerId(Integer bannerId) {
        this.bannerId = bannerId;
    }

    public LocalDateTime getOnlineDate() {
        return onlineDate;
    }

    public void setOnlineDate(LocalDateTime onlineDate) {
        this.onlineDate = onlineDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public LostCase getLostCase() {
        return lostCase;
    }

    public void setLostCase(LostCase lostCase) {
        this.lostCase = lostCase;
    }
}
