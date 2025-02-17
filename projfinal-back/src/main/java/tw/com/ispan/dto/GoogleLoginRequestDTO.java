package tw.com.ispan.dto;

public class GoogleLoginRequestDTO {
    private String idtoken;

    @Override
    public String toString() {
        return "GoogleLoginRequestDTO [idtoken=" + idtoken + "]";
    }

    public String getIdtoken() {
        return idtoken;
    }

    public void setIdtoken(String idtoken) {
        this.idtoken = idtoken;
    }

}