package tw.com.ispan.domain.pet;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import tw.com.ispan.domain.pet.RescueCase;
import tw.com.ispan.domain.pet.LostCase;
import tw.com.ispan.domain.pet.AdoptionCase;
import tw.com.ispan.domain.admin.Member;

@Entity
@Table(name = "ReportCase", uniqueConstraints = @UniqueConstraint(name = "UK_Member_Report", columnNames = { "memberId",
        "rescueCaseId", "lostCaseId", "adoptionCaseId", "reportType" }))
public class ReportCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增流水號
    @Column(name = "reportId")
    private Integer reportId;

    @ManyToOne
    @JoinColumn(name = "rescueCaseId", foreignKey = @ForeignKey(name = "FK_ReportCase_RescueCase"))
    private RescueCase rescueCase;

    @ManyToOne
    @JoinColumn(name = "lostCaseId", foreignKey = @ForeignKey(name = "FK_ReportCase_LostCase"))
    private LostCase lostCase;

    @ManyToOne
    @JoinColumn(name = "adoptionCaseId", foreignKey = @ForeignKey(name = "FK_ReportCase_AdoptionCase"))
    private AdoptionCase adoptionCase;

    @ManyToOne(optional = false)
    @JoinColumn(name = "memberId", nullable = false, foreignKey = @ForeignKey(name = "FK_ReportCase_Member"))
    private Member member;

    @Column(name = "reportDate", nullable = false)
    private LocalDateTime reportDate;

    @Column(name = "reportType", length = 30, nullable = false)
    private String reportType;

    @Column(name = "reportNotes", columnDefinition = "TEXT")
    private String reportNotes;

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

    public AdoptionCase getAdoptionCase() {
        return adoptionCase;
    }

    public void setAdoptionCase(AdoptionCase adoptionCase) {
        this.adoptionCase = adoptionCase;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public LocalDateTime getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDateTime reportDate) {
        this.reportDate = reportDate;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getReportNotes() {
        return reportNotes;
    }

    public void setReportNotes(String reportNotes) {
        this.reportNotes = reportNotes;
    }
}
