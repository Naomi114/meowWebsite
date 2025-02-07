package tw.com.ispan.dto;

public class CityDTO {
    private Integer cityId;
    private String city;

    // Constructor
    public CityDTO(Integer cityId, String city) {
        this.cityId = cityId;
        this.city = city;
    }

    // Getters and Setters
    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
