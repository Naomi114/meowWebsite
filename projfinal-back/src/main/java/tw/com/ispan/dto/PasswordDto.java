package tw.com.ispan.dto;

public class PasswordDto {

    private String email; // 用戶的電子郵件
    private String oldPassword; // 舊密碼
    private String newPassword; // 新密碼

    // Getters and Setters
    public String getEmail() { // 更改為 getEmail()
        return email;
    }

    public void setEmail(String email) { // 更改為 setEmail()
        this.email = email;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
