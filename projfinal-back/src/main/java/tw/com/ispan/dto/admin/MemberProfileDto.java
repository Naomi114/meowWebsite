package tw.com.ispan.dto.admin;

public class MemberProfileDto {
    private String lineName;
    private String linePicture;
    private String email;

    public MemberProfileDto(String lineName, String linePicture, String email) {
        this.lineName = lineName;
        this.linePicture = linePicture;
        this.email = email;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getLinePicture() {
        return linePicture;
    }

    public void setLinePicture(String linePicture) {
        this.linePicture = linePicture;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
