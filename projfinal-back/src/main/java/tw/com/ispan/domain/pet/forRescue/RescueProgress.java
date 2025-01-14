package tw.com.ispan.domain.pet.forRescue;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "RescueProgress")
public class RescueProgress {

	@Id
	@Column(name = "rescueProgressId")
	private Integer rescueProgressId;

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
