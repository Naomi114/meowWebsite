package tw.com.ispan.domain.pet;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import tw.com.ispan.domain.admin.Admin;
import tw.com.ispan.domain.admin.Member;

@Entity
@Table(name = "ReportCase", uniqueConstraints = @UniqueConstraint(name = "UK_Member_Report", columnNames = { "memberId",
        "adminId", "rescueCaseId", "lostCaseId", "adoptionCaseId", "reportTitle" }))
public class ReportCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增流水號
    @Column(name = "reportId")
    private Integer reportId;

    @ManyToOne
    @JoinColumn(name = "rescueCaseId", foreignKey = @ForeignKey(name = "FK_ReportCase_RescueCase"), nullable = true)
    private RescueCase rescueCase;

    // 關聯到 LostCase 表，單向多對一
    @ManyToOne
    @JoinColumn(name = "lostCaseId", foreignKey = @ForeignKey(name = "FK_ReportCase_LostCase"), nullable = true)
    private LostCase lostCase;

    @ManyToOne
    @JoinColumn(name = "adoptionCaseId", foreignKey = @ForeignKey(name = "FK_ReportCase_AdoptionCase"), nullable = true)
    private AdoptionCase adoptionCase;

    // 關聯到 Member 表，單向多對一
    @ManyToOne(optional = false)
    @JoinColumn(name = "memberId", nullable = false, foreignKey = @ForeignKey(name = "FK_ReportCase_Member"))
    private Member member;

    // 關聯到 Admin 表，單向多對一
    @ManyToOne
    @JoinColumn(name = "adminId", nullable = true, foreignKey = @ForeignKey(name = "FK_ReportCase_Admin"))
    private Admin admin;

    @Column(name = "reportDate", nullable = false)
    private LocalDateTime reportDate;

    @Column(name = "updateDate", nullable = true)
    private LocalDateTime updateDate;

    @Column(name = "reportTitle", length = 30, nullable = false)
    private String reportTitle;

    @Column(name = "reportNotes", columnDefinition = "TEXT")
    private String reportNotes;

    @Column(name = "reportState")
    private boolean reportState;

    // 空參數建構子 (Hibernate 要求)
    public ReportCase() {
        super();
    }

    // 全參數建構子
    public ReportCase(Integer reportId, RescueCase rescueCase, LostCase lostCase,
            // AdoptionCase adoptionCase,
            Member member, Admin admin, LocalDateTime reportDate, String reportTitle, String reportNotes,
            boolean reportState, LocalDateTime updateDate) {
        this.reportId = reportId;
        this.rescueCase = rescueCase;
        this.lostCase = lostCase;
        // this.adoptionCase=adoptionCase;
        this.member = member;
        this.admin = admin;
        this.reportDate = reportDate;
        this.reportTitle = reportTitle;
        this.reportNotes = reportNotes;
        this.reportState = reportState;
        this.updateDate = updateDate;
    }

    @Override
    public String toString() {
        return "ReportCase [reportId=" + reportId +
                ", lostCaseId=" + lostCase + ", rescueCaseId=" + rescueCase +
                // ", adoptionCaseId=" + adoptionCase +
                ",memberId=" + member + ",adminId=" + admin +
                ", reportDate=" + reportDate + ", reportTitle=" + reportTitle + ", reportNotes=" + reportNotes
                + ", reportState=" + reportState + ", updateDate=" + updateDate + "]";
    }

    // Getters and Setters
    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public RescueCase getRescueCase() {
        return rescueCase;
    }

    public void setRescueCase(RescueCase rescueCase) {
        this.rescueCase = rescueCase;
    }

    public LostCase getLostCase() {
        return lostCase;
    }

    public void setLostCase(LostCase lostCase) {
        this.lostCase = lostCase;
    }

    // public AdoptionCase getAdoptionCase() {
    // return adoptionCase;
    // }

    // public void setAdoptionCase(AdoptionCase adoptionCase) {
    // this.adoptionCase = adoptionCase;
    // }

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

    public LocalDateTime getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDateTime reportDate) {
        this.reportDate = reportDate;
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    public String getReportNotes() {
        return reportNotes;
    }

    public void setReportNotes(String reportNotes) {
        this.reportNotes = reportNotes;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public boolean isReportState() {
        return reportState;
    }

    public void setReportState(boolean reportState) {
        this.reportState = reportState;
    }

}
