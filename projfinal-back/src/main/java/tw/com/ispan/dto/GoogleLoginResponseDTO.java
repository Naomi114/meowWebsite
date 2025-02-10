package tw.com.ispan.dto;

public class GoogleLoginResponseDTO {

    private boolean success;
    private String message;
    private String email;
    private String nickname;
    private String userId; // 可能需要的額外屬性
    private String profilePicture; // 例如：使用者的頭像

    // Constructor
    public GoogleLoginResponseDTO(boolean success, String message, String email, String nickname, String userId,
            String profilePicture) {
        this.success = success;
        this.message = message;
        this.email = email;
        this.nickname = nickname;
        this.userId = userId;
        this.profilePicture = profilePicture;
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
