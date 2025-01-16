// package tw.com.ispan.domain.pet;

// import java.time.LocalDateTime;

// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.ForeignKey;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.ManyToOne;
// import jakarta.persistence.Table;
// import jakarta.persistence.UniqueConstraint;
// // import tw.com.ispan.domain.admin.Member;

// @Entity
// @Table(name = "ReportCase", uniqueConstraints = @UniqueConstraint(name =
// "UK_Member_Report", columnNames = { "memberId",
// "rescueCaseId", "lostCaseId", "adoptionCaseId", "reportType" }))
// public class ReportCase {

// @Id
// @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增流水號
// @Column(name = "reportId")
// private Integer reportId;

// @ManyToOne
// @JoinColumn(name = "rescueCaseId", foreignKey = @ForeignKey(name =
// "FK_ReportCase_RescueCase"), nullable = true)
// @JsonBackReference
// private RescueCase rescueCase;

// // 關聯到 LostCase 表，單向多對一
// @ManyToOne
// @JoinColumn(name = "lostCaseId", foreignKey = @ForeignKey(name =
// "FK_ReportCase_LostCase"), nullable = true)
// @JsonBackReference
// private LostCase lostCase;

// // @ManyToOne
// // @JoinColumn(name = "adoptionCaseId", foreignKey = @ForeignKey(name =
// // "FK_ReportCase_AdoptionCase"), nullable = true)
// // @JsonBackReference
// // private AdoptionCase adoptionCase;

// // 關聯到 Member 表，單向多對一
// // @ManyToOne(optional = false)
// // @JoinColumn(name = "memberId", nullable = false, foreignKey =
// // @ForeignKey(name = "FK_ReportCase_Member"))
// // private Member member;

// @Column(name = "reportDate", nullable = false)
// private LocalDateTime reportDate;

// @Column(name = "reportType", length = 30, nullable = false)
// private String reportType;

// @Column(name = "reportNotes", columnDefinition = "TEXT")
// private String reportNotes;

// @Override
// public String toString() {
// return "ReportCase [reportId=" + reportId +
// ", lostCaseId=" + lostCase +
// // ", rescueCaseId=" + rescueCase +", adoptionCaseId=" + adoptionCase +",
// // memberId=" + member +
// ", reportDate=" + reportDate
// + ", reportType=" + reportType + ", reportNotes=" + reportNotes + "]";
// }

// // Getters and Setters
// public Integer getReportId() {
// return reportId;
// }

// public void setReportId(Integer reportId) {
// this.reportId = reportId;
// }

// // public RescueCase getRescueCase() {
// // return rescueCase;
// // }

// // public void setRescueCase(RescueCase rescueCase) {
// // this.rescueCase = rescueCase;
// // }

// public LostCase getLostCase() {
// return lostCase;
// }

// public void setLostCase(LostCase lostCase) {
// this.lostCase = lostCase;
// }

// // public AdoptionCase getAdoptionCase() {
// // return adoptionCase;
// // }

// // public void setAdoptionCase(AdoptionCase adoptionCase) {
// // this.adoptionCase = adoptionCase;
// // }

// // public Member getMember() {
// // return member;
// // }

// // public void setMember(Member member) {
// // this.member = member;
// // }

// public LocalDateTime getReportDate() {
// return reportDate;
// }

// public void setReportDate(LocalDateTime reportDate) {
// this.reportDate = reportDate;
// }

// public String getReportType() {
// return reportType;
// }

// public void setReportType(String reportType) {
// this.reportType = reportType;
// }

// public String getReportNotes() {
// return reportNotes;
// }

// public void setReportNotes(String reportNotes) {
// this.reportNotes = reportNotes;
// }
// }
