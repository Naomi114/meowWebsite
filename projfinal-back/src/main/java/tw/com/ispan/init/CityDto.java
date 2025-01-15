package tw.com.ispan.util;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//標記此可忽略未定義的檔案內屬性
@JsonIgnoreProperties(ignoreUnknown = true)
public class CityDto {
	
	    private String cityName;
	    private List<AreaDto> areaList;
		
	    
	    //json資料轉換過程涉及getter setter
	    public String getCityName() {
			return cityName;
		}
		public void setCityName(String cityName) {
			this.cityName = cityName;
		}
		public List<AreaDto> getAreaList() {
			return areaList;
		}
		public void setAreaList(List<AreaDto> areaList) {
			this.areaList = areaList;
		}
	    
	    
	   
	    
}
