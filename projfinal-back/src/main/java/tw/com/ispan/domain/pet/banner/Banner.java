package tw.com.ispan.domain.pet.banner;

import jakarta.persistence.*;
import tw.com.ispan.domain.pet.AdoptionCase;
import tw.com.ispan.domain.pet.LostCase;
import tw.com.ispan.domain.pet.RescueCase;

import java.time.LocalDateTime;

@Entity
@Table(name = "Banner")
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bannerId;

    @Column(name = "onlineDate", nullable = false)
    private LocalDateTime onlineDate;

    @Column(name = "dueDate", nullable = false)
    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "bannerType", nullable = false)
    private BannerType bannerType;

    @OneToOne
    @JoinColumn(name = "lostCaseId", nullable = true)
    private LostCase lostCase;

    @OneToOne
    @JoinColumn(name = "adoptionCaseId", nullable = true)
    private AdoptionCase adoptionCase;

    @OneToOne
    @JoinColumn(name = "rescueCaseId", nullable = true)
    private RescueCase rescueCase;

    @Column(name = "isHidden", nullable = false)
    private Boolean isHidden = false;

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

    public BannerType getBannerType() {
        return bannerType;
    }

    public void setBannerType(BannerType bannerType) {
        this.bannerType = bannerType;
    }

    public LostCase getLostCase() {
        return lostCase;
    }

    public void setLostCase(LostCase lostCase) {
        this.lostCase = lostCase;
    }

    public AdoptionCase getAdoptionCase() {
        return adoptionCase;
    }

    public void setAdoptionCase(AdoptionCase adoptionCase) {
        this.adoptionCase = adoptionCase;
    }

    public RescueCase getRescueCase() {
        return rescueCase;
    }

    public void setRescueCase(RescueCase rescueCase) {
        this.rescueCase = rescueCase;
    }

    public Boolean getIsHidden() {
        return isHidden;
    }

    public void setIsHidden(Boolean isHidden) {
        this.isHidden = isHidden;
    }
}
