package tw.com.ispan.projfinal_back.domain.pet;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import tw.com.ispan.projfinal_back.domain.admin.Member;

@Entity
@Table(name = "Activity")
public class Activity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "activityId")
	private Integer activityId;

	// 和member表為雙向多對一
	@ManyToOne
	@JoinColumn(name = "memberId", nullable = false)
	private Member member;

	@Column(name = "title", columnDefinition = "nvarchar(30)", nullable = false)
	private String title;

	@Column(name = "purpose", columnDefinition = "nvarchar(30)")
	private String purpose;

	@Lob
	@Column(name = "description", columnDefinition = "nvarchar(max)")
	private String description;

	@Column(name = "location", columnDefinition = "nvarchar(max)")
	private String location;

	@Column(name = "startTime")
	private LocalDateTime startTime;

	@Column(name = "endTime")
	private LocalDateTime endTime;

	@Column(name = "maxParticipants")
	private Integer maxParticipants;

	@Column(name = "createdAt")
	private LocalDateTime createdAt;

	@Column(name = "updatedAt")
	private LocalDateTime updatedAt;

	@Column(name = "activityStatus", columnDefinition = "varchar(5)")
	private String activityStatus;

	@Column(name = "adminPermission")
	private Boolean adminPermission;

	@Column(name = "reward")
	private Integer reward;

	@OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<ActivityParticipantList> participantLists;
}
