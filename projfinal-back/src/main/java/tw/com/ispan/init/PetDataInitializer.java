package tw.com.ispan.init;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import tw.com.ispan.domain.pet.Breed;
import tw.com.ispan.domain.pet.CaseState;
import tw.com.ispan.domain.pet.City;
import tw.com.ispan.domain.pet.DistinctArea;
import tw.com.ispan.domain.pet.FurColor;
import tw.com.ispan.domain.pet.Species;
import tw.com.ispan.domain.pet.forRescue.CanAfford;
import tw.com.ispan.domain.pet.forRescue.RescueDemand;
import tw.com.ispan.repository.pet.BreedRepository;
import tw.com.ispan.repository.pet.CaseStateRepository;
import tw.com.ispan.repository.pet.CityRepository;
import tw.com.ispan.repository.pet.FurColorRepository;
import tw.com.ispan.repository.pet.SpeciesRepository;
import tw.com.ispan.repository.pet.forRescue.CanAffordRepository;
import tw.com.ispan.repository.pet.forRescue.RescueDemandRepository;

@Component
public class PetDataInitializer implements CommandLineRunner {

	@Autowired
	private SpeciesRepository speciesRepository;
	@Autowired
	private CityRepository cityRepository;
	@Autowired
	private FurColorRepository furColorRepository;
	@Autowired
	private BreedRepository breedRepository;
	@Autowired
	CaseStateRepository caseStateRepository;
	@Autowired
	RescueDemandRepository rescueDemandRepository;
	@Autowired
	CanAffordRepository canAffordRepository;

	// 此方法會在專案啟動同時執行一次，進行資料初始化
	@Override
	public void run(String... args) throws Exception {

		// 存入物種資料
		if (!speciesRepository.existsById(1)) {
			speciesRepository.save(new Species("狗"));
		}
		if (!speciesRepository.existsById(2)) {
			speciesRepository.save(new Species("貓"));
		}

		// 存入品種資料(狗貓放在同一表格，貓breedId為1~53 狗breedId 54~186)
		//檢查邏輯為breed資料表內是否有id 1-53的資料，返回的list如果不是共53筆就做新增 (其實邏輯不太對，但基本上有執行過資料注入就應該一次是53筆，就先這樣吧)
		List<Integer> catList = breedRepository.findBreedIdsInRange(1,53);
		if(catList.size() != 53) {
			Resource catResource = new ClassPathResource("data/catBreeds.json");
			ObjectMapper objectMapper1 = new ObjectMapper();

			try {
				List<Breed> catBreed = objectMapper1.readValue(catResource.getInputStream(),
						new TypeReference<List<Breed>>() {
						});
				catBreed.forEach(breed -> breedRepository.save(breed));
			} catch (IOException e) {
				System.out.println("資料注入失敗");
				e.printStackTrace();
			}
		}
		
		List<Integer> dogList = breedRepository.findBreedIdsInRange(54,186);
		if(dogList.size() != 133) {
			Resource dogResource = new ClassPathResource("data/dogBreeds.json");
			ObjectMapper objectMapper2 = new ObjectMapper();

			try {
				List<Breed> dogBreed = objectMapper2.readValue(dogResource.getInputStream(),
						new TypeReference<List<Breed>>() {
						});
				dogBreed.forEach(breed -> breedRepository.save(breed));
			} catch (IOException e) {
				System.out.println("資料注入失敗");
				e.printStackTrace();
			}
		}

		

		// 存入毛色資料(主要給米克斯用)
		if (!furColorRepository.existsById(1)) {
			furColorRepository.save(new FurColor("土黃"));
		}
		if (!furColorRepository.existsById(2)) {
			furColorRepository.save(new FurColor("白"));
		}
		if (!furColorRepository.existsById(3)) {
			furColorRepository.save(new FurColor("黑"));
		}
		if (!furColorRepository.existsById(4)) {
			furColorRepository.save(new FurColor("橘白貓"));
		}
		if (!furColorRepository.existsById(5)) {
			furColorRepository.save(new FurColor("三花貓"));
		}
		if (!furColorRepository.existsById(6)) {
			furColorRepository.save(new FurColor("虎斑"));
		}
		if (!furColorRepository.existsById(7)) {
			furColorRepository.save(new FurColor("賓士"));
		}

		// 存入casestate (狀態描述 ( 認養: 待認養/已認養; 救援: 待救援/已救援; 協尋: 待協尋/已尋回 三種共用: 變成小天使、案件失敗))
		if (!caseStateRepository.existsById(1)) {
			caseStateRepository.save(new CaseState("待認養"));
		}
		if (!caseStateRepository.existsById(2)) {
			caseStateRepository.save(new CaseState("已認養"));
		}
		if (!caseStateRepository.existsById(3)) {
			caseStateRepository.save(new CaseState("待救援"));
		}
		if (!caseStateRepository.existsById(4)) {
			caseStateRepository.save(new CaseState("已救援"));
		}
		if (!caseStateRepository.existsById(5)) {
			caseStateRepository.save(new CaseState("待協尋"));
		}
		if (!caseStateRepository.existsById(6)) {
			caseStateRepository.save(new CaseState("已尋回"));
		}
		if (!caseStateRepository.existsById(7)) {
			caseStateRepository.save(new CaseState("變成小天使"));
		}
		if (!caseStateRepository.existsById(8)) {
			caseStateRepository.save(new CaseState("案件失敗"));
		}

		// 存入RescueDemand ((尋求抓紮協助/尋求安置協助/需就醫/尋求誘捕協助))
		if (!rescueDemandRepository.existsById(1)) {
			rescueDemandRepository.save(new RescueDemand("尋求抓紮協助"));
		}
		if (!rescueDemandRepository.existsById(2)) {
			rescueDemandRepository.save(new RescueDemand("尋求安置協助"));
		}
		if (!rescueDemandRepository.existsById(3)) {
			rescueDemandRepository.save(new RescueDemand("需就醫"));
		}
		if (!rescueDemandRepository.existsById(4)) {
			rescueDemandRepository.save(new RescueDemand("尋求誘捕協助"));
		}

		// 存入canAfford (可提供安置照顧空間/救援後可自行收編/無法負擔任何事項/願意負擔救援所需物資/願意負擔救援所需費用)
		if (!canAffordRepository.existsById(1)) {
			canAffordRepository.save(new CanAfford("可提供安置照顧空間"));
		}
		if (!canAffordRepository.existsById(2)) {
			canAffordRepository.save(new CanAfford("救援後可自行收編"));
		}
		if (!canAffordRepository.existsById(3)) {
			canAffordRepository.save(new CanAfford("無法負擔任何事項"));
		}
		if (!canAffordRepository.existsById(4)) {
			canAffordRepository.save(new CanAfford("願意負擔救援物資"));
		}
		if (!canAffordRepository.existsById(1)) {
			canAffordRepository.save(new CanAfford("願意負擔救援費用"));
		}

		// 存入city資料(舊版!!!!)
		// 臺北市、新北市、基隆市、新竹市、桃園市、新竹縣及宜蘭縣。 中部區域：包括臺中市、苗栗縣、彰化縣、南投縣及雲林縣。
		// 南部區域：包括高雄市、臺南市、嘉義市、嘉義縣、屏東縣及澎湖縣。 東部區域：包括花蓮縣及臺東縣
//		if (!cityRepository.existsById(1)) {
//			cityRepository.save(new City("臺北市"));
//		}
//		if (!cityRepository.existsById(2)) {
//			cityRepository.save(new City("新北市"));
//		}
//		if (!cityRepository.existsById(3)) {
//			cityRepository.save(new City("基隆市"));
//		}
//		if (!cityRepository.existsById(4)) {
//			cityRepository.save(new City("新竹市"));
//		}
//		if (!cityRepository.existsById(5)) {
//			cityRepository.save(new City("桃園市"));
//		}
//		if (!cityRepository.existsById(6)) {
//			cityRepository.save(new City("新竹縣"));
//		}
//		if (!cityRepository.existsById(7)) {
//			cityRepository.save(new City("宜蘭縣"));
//		}
//		if (!cityRepository.existsById(8)) {
//			cityRepository.save(new City("臺中市"));
//		}
//		if (!cityRepository.existsById(9)) {
//			cityRepository.save(new City("苗栗縣"));
//		}
//		if (!cityRepository.existsById(10)) {
//			cityRepository.save(new City("彰化縣"));
//		}
//		if (!cityRepository.existsById(11)) {
//			cityRepository.save(new City("南投縣"));
//		}
//		if (!cityRepository.existsById(12)) {
//			cityRepository.save(new City("雲林縣"));
//		}
//		if (!cityRepository.existsById(13)) {
//			cityRepository.save(new City("高雄市"));
//		}
//		if (!cityRepository.existsById(14)) {
//			cityRepository.save(new City("臺南市"));
//		}
//		if (!cityRepository.existsById(15)) {
//			cityRepository.save(new City("嘉義市"));
//		}
//		if (!cityRepository.existsById(16)) {
//			cityRepository.save(new City("嘉義縣"));
//		}
//		if (!cityRepository.existsById(17)) {
//			cityRepository.save(new City("屏東縣"));
//		}
//		if (!cityRepository.existsById(18)) {
//			cityRepository.save(new City("澎湖縣"));
//		}
//		if (!cityRepository.existsById(19)) {
//			cityRepository.save(new City("花蓮縣"));
//		}
//		if (!cityRepository.existsById(20)) {
//			cityRepository.save(new City("臺東縣"));
//		}
//		if (!cityRepository.existsById(21)) {
//			cityRepository.save(new City("金門縣"));
//		}
//		if (!cityRepository.existsById(22)) {
//			cityRepository.save(new City("連江縣"));
//		}

		// 存入distinct資料
		// Jackson 或 Gson 在將 JSON 轉換為
		// Java物件時，只會映射與dto類別中字段名稱匹配的JSON屬性(大小寫敏感)，額外的屬性會被自動忽略，而不會影響轉換過程
		// 檔案位於 resources 資料夾內，建議使用 ClassLoader 來讀取檔案，這樣可以避免路徑解析問題
		// 這段在跑測試程式時，因為測試程式和專案啟動執行環境不同，僅測試不會去打包resource底下靜態資源，因此data/CityCountyData.json會找不到，導致需要先註解!!

		//city和distinct資料會同時儲存，因此檢查city是否存在即可(邏輯不太完善但先這樣吧)
		List<Integer> cityList = cityRepository.findCityIdsInRange(1,24);
		if(cityList.size() != 24) {
			String filePath = getClass().getClassLoader().getResource("data/CityCountyData.json").getPath();
			ObjectMapper objectMapper3 = new ObjectMapper();

			List<CityDto> cityDtoList = objectMapper3.readValue(new File(filePath), new TypeReference<List<CityDto>>() {
			});

			for (CityDto cityDto : cityDtoList) {
				City city = new City();
				city.setCity(cityDto.getCityName());

				List<DistinctArea> areas = cityDto.getAreaList().stream().map(areaDto -> {
					DistinctArea area = new DistinctArea();
					area.setDistinctAreaName(areaDto.getAreaName());
					return area;
				}).toList();

				city.setDistinctAreas(areas);

				// 儲存 City（會同時儲存其相關的 DistinctArea）
				cityRepository.save(city);
			}
		}
		
	}

}
