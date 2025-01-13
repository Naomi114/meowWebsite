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
//
//	@ManyToOne
//	@JoinColumn(name = "lostCaseId", nullable = true)
//	private LostCase lostCase;
//
//	@ManyToOne
//	@JoinColumn(name = "adoptionCaseId", nullable = true)
//	private AdoptionCase adoptionCase;

	@Column(name = "followDate")
	private LocalDateTime followDate;

	@Column(name = "notifications_enabled")
	private Boolean notificationsEnabled;
}
