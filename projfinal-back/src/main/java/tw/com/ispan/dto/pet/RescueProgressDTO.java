package tw.com.ispan.dto.pet;

import java.util.Collections;
import java.util.List;

public class RescueProgressDTO {

	private String progressDetail;
	private List<String> imageUrl;

	public String getProgressDetail() {
		return progressDetail;
	}

	public void setProgressDetail(String progressDetail) {
		this.progressDetail = progressDetail;
	}

	//因為前端有可能傳空陣列進來，為避免後續存取進實體中時有null，因此若沒傳圖片，則改為空陣列
	public List<String> getImageUrl() {
	    return imageUrl == null ? Collections.emptyList() : imageUrl;
	}
	
	
	public void setImageUrl(List<String> imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public String toString() {
		return "RescueProgressDTO [progressDetail=" + progressDetail + ", imageUrl=" + imageUrl + "]";
	}
	
	
}
