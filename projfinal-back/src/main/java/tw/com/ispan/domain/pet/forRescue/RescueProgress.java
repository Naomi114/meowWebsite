package tw.com.ispan.domain.pet.forRescue;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import tw.com.ispan.domain.pet.RescueCase;

@Entity
@Table(name = "RescueProgress")
public class RescueProgress {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rescueProgressId")
	private Integer rescueProgressId;
	
	@JsonIgnore // 忽略序列化此字段
	@ManyToOne
	@JoinColumn(name="rescueCaseId", nullable = false)
	private RescueCase rescueCase;

	@Column(name = "progressDetail", columnDefinition = "nvarchar(max)")
	private String progressDetail;

	@Column(name = "createTime")
	private LocalDateTime createTime;

	@Column(name = "imageUrl", length = 255)
	private String imageUrl;
	
	
	public RescueProgress() {
		super();
	}


	public Integer getRescueProgressId() {
		return rescueProgressId;
	}


	public void setRescueProgressId(Integer rescueProgressId) {
		this.rescueProgressId = rescueProgressId;
	}
	
	
	public RescueCase getRescueCase() {
		return rescueCase;
	}


	public void setRescueCase(RescueCase rescueCase) {
		this.rescueCase = rescueCase;
	}


	public String getProgressDetail() {
		return progressDetail;
	}


	public void setProgressDetail(String progressDetail) {
		this.progressDetail = progressDetail;
	}


	public LocalDateTime getCreateTime() {
		return createTime;
	}


	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}


	public String getImageUrl() {
		return imageUrl;
	}


	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}


	@Override
	public String toString() {
		return "RescueProgress [rescueProgressId=" + rescueProgressId + ", progressDetail=" + progressDetail
				+ ", createTime=" + createTime + ", imageUrl=" + imageUrl + "]";
	}

	
	
}
