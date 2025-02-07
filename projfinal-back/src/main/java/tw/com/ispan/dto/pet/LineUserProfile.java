package tw.com.ispan.dto.pet;

public class LineUserProfile {
	private String userId;
	private String displayName;
	private String pictureUrl;
	private String statusMessage;

	// Getters and Setters
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	@Override
	public String toString() {
		return "LineUserProfile [userId=" + userId + ", displayName=" + displayName + ", pictureUrl=" + pictureUrl
				+ ", statusMessage=" + statusMessage + ", getUserId()=" + getUserId() + ", getDisplayName()="
				+ getDisplayName() + ", getPictureUrl()=" + getPictureUrl() + ", getStatusMessage()="
				+ getStatusMessage() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}

}
