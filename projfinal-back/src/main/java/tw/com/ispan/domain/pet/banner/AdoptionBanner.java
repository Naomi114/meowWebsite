// package tw.com.ispan.domain.pet.banner;

// import java.time.LocalDateTime;

// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.ForeignKey;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.OneToOne;
// import jakarta.persistence.Table;
// import tw.com.ispan.domain.pet.AdoptionCase;

// @Entity
// @Table(name = "AdoptBanner")
// public class AdoptionBanner {

// @Id
// @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增流水號
// @Column(name = "bannerId")
// private Integer bannerId;

// @Column(name = "OnlineDate", nullable = false)
// private LocalDateTime onlineDate;

// @Column(name = "DueDate", nullable = false)
// private LocalDateTime dueDate;

// @OneToOne
// @JoinColumn(name = "adoptionCaseId", referencedColumnName = "adoptionCaseId")
// private AdoptionCase adoptionCase;

// // Getters and Setters
// public Integer getBannerId() {
// return bannerId;
// }

// public void setBannerId(Integer bannerId) {
// this.bannerId = bannerId;
// }

// public LocalDateTime getOnlineDate() {
// return onlineDate;
// }

// public void setOnlineDate(LocalDateTime onlineDate) {
// this.onlineDate = onlineDate;
// }

// public LocalDateTime getDueDate() {
// return dueDate;
// }

// public void setDueDate(LocalDateTime dueDate) {
// this.dueDate = dueDate;
// }

// public AdoptionCase getAdoptionCase() {
// return adoptionCase;
// }

// public void setAdoptionCase(AdoptionCase adoptionCase) {
// this.adoptionCase = adoptionCase;
// }
// }
