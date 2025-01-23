package tw.com.ispan.domain.pet;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import tw.com.ispan.domain.admin.Member;

//案件和會員的追蹤中介表 (須從會員查詢追蹤那些案件 因此member對follow為雙向 follow對case為單向。但case本身又需要看被多少人追蹤，因此case,follow也設成雙向)
//索引:由於添加follow表資料前會查詢是否存在某memberid+某caseId的組合，因此添加複合索引
@Entity
@Table(name = "Follow")
public class Follow {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "followId")
	private Integer followId;

	@ManyToOne
	@JoinColumn(name = "memberId", nullable = false)
	private Member member;

	@ManyToOne
	@JoinColumn(name = "rescueCaseId", nullable = true)
	private RescueCase rescueCase;

	@ManyToOne
	@JoinColumn(name = "lostCaseId", nullable = true)
	private LostCase lostCase;

	@ManyToOne
	@JoinColumn(name = "adoptionCaseId", nullable = true)
	private AdoptionCase adoptionCase;

	@Column(name = "followDate")
	private LocalDateTime followDate;

	@Column(name = "notifications_enabled")
	private Boolean notificationsEnabled;

	
	
	//getter & setter
	public Integer getFollowId() {
		return followId;
	}

	public void setFollowId(Integer followId) {
		this.followId = followId;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
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

	public LocalDateTime getFollowDate() {
		return followDate;
	}

	public void setFollowDate(LocalDateTime followDate) {
		this.followDate = followDate;
	}

	public Boolean getNotificationsEnabled() {
		return notificationsEnabled;
	}

	public void setNotificationsEnabled(Boolean notificationsEnabled) {
		this.notificationsEnabled = notificationsEnabled;
	}
	
	
	
}
