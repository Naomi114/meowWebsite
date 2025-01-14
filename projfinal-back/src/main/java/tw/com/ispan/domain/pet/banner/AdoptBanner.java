package tw.com.ispan.domain.pet.banner;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import tw.com.ispan.domain.pet.AdoptionCase;

@Entity
@Table(name = "AdoptBanner")
public class AdoptBanner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增流水號
    @Column(name = "bannerId")
    private Integer bannerId;

    @Column(name = "OnlineDate", nullable = false)
    private LocalDateTime onlineDate;

    @Column(name = "DueDate", nullable = false)
    private LocalDateTime dueDate;

    @OneToOne
    @JoinColumn(name = "adoptCaseId", nullable = false, unique = true, foreignKey = @ForeignKey(name = "FK_AdoptBanner_AdoptCase"))
    private AdoptionCase adoptionCase;

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

}
