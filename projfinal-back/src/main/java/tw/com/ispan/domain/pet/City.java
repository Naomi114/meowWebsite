package tw.com.ispan.projfinal_back.domain.pet;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "City")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cityId;

    @Column(name = "city", columnDefinition = "NVARCHAR(5)", nullable = false)
    private String city;

    //和RescueCase表雙向一對多
    @OneToMany(mappedBy = "cityId", cascade = CascadeType.PERSIST)
    private List<RescueCase> rescueCases;

    
	public City() {
		super();
	}


	public City(Integer cityId, String city) {
		super();
		this.cityId = cityId;
		this.city = city;
	}


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


	public List<RescueCase> getRescueCases() {
		return rescueCases;
	}


	public void setRescueCases(List<RescueCase> rescueCases) {
		this.rescueCases = rescueCases;
	}

}
