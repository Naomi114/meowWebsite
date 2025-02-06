package tw.com.ispan.init.pet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AreaDto {

	private Integer districtAreaId;
	private String areaName;

	// json資料轉換過程涉及getter setter

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public Integer getDistrictAreaId() {
		return districtAreaId;
	}

	public void setDistrictAreaId(Integer districtAreaId) {
		this.districtAreaId = districtAreaId;
	}

}
