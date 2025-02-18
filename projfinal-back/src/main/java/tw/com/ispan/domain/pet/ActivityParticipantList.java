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
@Table(name = "ActivityParticipantList")
public class ActivityParticipantList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participantId")
    private Integer participantId;

    //雙向多對一
    @ManyToOne
    @JoinColumn(name = "activityId", nullable = false)
    private Activity activity;

    //雙向多對一
    @ManyToOne
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    @Column(name = "registrationTime")
    private LocalDateTime registrationTime;

    @Column(name = "status", length = 5)
    private String status;
}
