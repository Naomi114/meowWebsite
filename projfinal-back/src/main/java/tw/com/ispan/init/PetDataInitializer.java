package tw.com.ispan.init;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import tw.com.ispan.domain.pet.Breed;
import tw.com.ispan.domain.pet.CasePicture;
import tw.com.ispan.domain.pet.CaseState;
import tw.com.ispan.domain.pet.City;
import tw.com.ispan.domain.pet.DistrictArea;
import tw.com.ispan.domain.pet.FurColor;
import tw.com.ispan.domain.pet.RescueCase;
import tw.com.ispan.domain.pet.Species;
import tw.com.ispan.domain.pet.forRescue.CanAfford;
import tw.com.ispan.domain.pet.forRescue.RescueDemand;
import tw.com.ispan.init.pet.CityDto;
import tw.com.ispan.init.pet.fakeRescueCaseDto;
import tw.com.ispan.repository.admin.MemberRepository;
import tw.com.ispan.repository.pet.BreedRepository;
import tw.com.ispan.repository.pet.CaseStateRepository;
import tw.com.ispan.repository.pet.CityRepository;
import tw.com.ispan.repository.pet.DistrictAreaRepository;
import tw.com.ispan.repository.pet.FurColorRepository;
import tw.com.ispan.repository.pet.RescueCaseRepository;
import tw.com.ispan.repository.pet.SpeciesRepository;
import tw.com.ispan.repository.pet.forRescue.CanAffordRepository;
import tw.com.ispan.repository.pet.forRescue.RescueDemandRepository;
import tw.com.ispan.service.pet.ImageService;

@Transactional
@Component
@Order(3) // 最後執行
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
	private CaseStateRepository caseStateRepository;
	@Autowired
	private RescueDemandRepository rescueDemandRepository;
	@Autowired
	private CanAffordRepository canAffordRepository;
	@Autowired
	private RescueCaseRepository rescueCaseRepository;
	@Autowired
	private DistrictAreaRepository districtAreaRepository;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private ImageService imageService;

	public void saveSpeciesData() {
		if (!speciesRepository.existsById(1)) {
			speciesRepository.save(new Species("狗"));
		}
		if (!speciesRepository.existsById(2)) {
			speciesRepository.save(new Species("貓"));
		}
	}

	@Value("${file.final-upload-dir}") // 後端圖片最終路徑
	private String imageBaseUrl;

	// 此方法會在專案啟動同時執行一次，進行資料初始化
	@Override
	public void run(String... args) throws Exception {

		// 存入物種資料
		saveSpeciesData();
		// 手動檢查是否成功存入
		// if (!speciesRepository.existsById(1) || !speciesRepository.existsById(2)) {
		// throw new RuntimeException("Species 資料未成功儲存，請檢查交易提交狀態！");
		// }

		// 存入品種資料(狗貓放在同一表格，貓breedId為1~53 狗breedId 54~186)
		// 檢查邏輯為breed資料表內是否有id 1-53的資料，返回的list如果不是共53筆就做新增
		// (其實邏輯不太對，但基本上有執行過資料注入就應該一次是53筆，就先這樣吧)
		List<Integer> catList = breedRepository.findBreedIdsInRange(1, 53);
		if (catList.size() != 53) {
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

		List<Integer> dogList = breedRepository.findBreedIdsInRange(54, 186);
		if (dogList.size() != 133) {
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
		if (!furColorRepository.existsById(1))

		{
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
			furColorRepository.save(new FurColor("虎斑貓"));
		}
		if (!furColorRepository.existsById(7)) {
			furColorRepository.save(new FurColor("賓士貓"));
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
		if (!canAffordRepository.existsById(5)) {
			canAffordRepository.save(new CanAfford("願意負擔救援費用"));
		}

		// 存入district資料
		// Jackson 或 Gson 在將 JSON 轉換為
		// Java物件時，只會映射與dto類別中字段名稱匹配的JSON屬性(大小寫敏感)，額外的屬性會被自動忽略，而不會影響轉換過程
		// 檔案位於 resources 資料夾內，建議使用 ClassLoader 來讀取檔案，這樣可以避免路徑解析問題
		// 這段在跑測試程式時，因為測試程式和專案啟動執行環境不同，僅測試不會去打包resource底下靜態資源，因此data/CityCountyData.json會找不到，導致需要先註解!!

		// city和district資料會同時儲存，因此檢查city是否存在即可(邏輯不太完善但先這樣吧)
		List<Integer> cityList = cityRepository.findCityIdsInRange(1, 24);
		if (cityList.size() != 24) {
			String filePath = getClass().getClassLoader().getResource("data/CityCountyData.json").getPath();
			ObjectMapper objectMapper3 = new ObjectMapper();

			List<CityDto> cityDtoList = objectMapper3.readValue(new File(filePath), new TypeReference<List<CityDto>>() {
			});

			for (CityDto cityDto : cityDtoList) {
				City city = new City();
				city.setCity(cityDto.getCityName());

				List<DistrictArea> areas = cityDto.getAreaList().stream().map(areaDto -> {
					DistrictArea area = new DistrictArea();
					area.setDistrictAreaName(areaDto.getAreaName());
					area.setCity(city); // 關聯 City
					return area;
				}).toList();

				city.setDistrictAreas(areas);

				// 儲存 City（會同時儲存其相關的 DistrictArea）
				cityRepository.save(city);
			}
		}

		// 塞入5筆救援案件假資料以及更新圖片表

		if (rescueCaseRepository.count() < 5) {

			// 讀取 JSON 假資料
			ObjectMapper objectMapper = new ObjectMapper();
			String filePath = "data/rescueCase.json"; // JSON 檔案的路徑
			String jsonContent = new String(Files.readAllBytes(new ClassPathResource(filePath).getFile().toPath()));

			// 替換 ${final-upload-dir} 為環境變數的值，為圖片儲存於後端的外部資料夾路徑
			jsonContent = jsonContent.replace("${final-upload-dir}", imageBaseUrl);

			List<fakeRescueCaseDto> rescueCaseDtos = objectMapper.readValue(
					jsonContent,
					new TypeReference<List<fakeRescueCaseDto>>() {
					});

			// 將 DTO 轉換為實體並存入資料庫
			for (fakeRescueCaseDto dto : rescueCaseDtos) {
				RescueCase rescueCase = new RescueCase();

				// 儲存圖片路徑於圖片表中
				List<CasePicture> casePictures = imageService.saveImage(dto.getCasePictureUrls());

				// 設定 RescueCase 基本屬性
				rescueCase.setCaseTitle(dto.getCaseTitle());
				rescueCase.setGender(dto.getGender());
				rescueCase.setSterilization(dto.getSterilization());
				rescueCase.setAge(dto.getAge());
				rescueCase.setMicroChipNumber(dto.getMicroChipNumber());
				rescueCase.setSuspLost(dto.getSuspLost());
				rescueCase.setStreet(dto.getStreet());
				rescueCase.setLatitude(dto.getLatitude());
				rescueCase.setLongitude(dto.getLongitude());
				rescueCase.setDonationAmount(dto.getDonationAmount());
				rescueCase.setViewCount(dto.getViewCount());
				rescueCase.setFollow(dto.getFollow());
				rescueCase.setPublicationTime(LocalDateTime.now()); // 自動設置發佈時間
				rescueCase.setLastUpdateTime(LocalDateTime.now()); // 自動設置更新時間
				rescueCase.setTag(dto.getTag());
				rescueCase.setRescueReason(dto.getRescueReason());
				rescueCase.setCaseUrl(dto.getCaseUrl());
				rescueCase.setIsHidden(dto.getIsHidden());

				// 手動關聯實體
				rescueCase.setMember(memberRepository.findById(dto.getMemberId())
						.orElseThrow(() -> new RuntimeException("member not found")));

				System.out.println("物種ID" + dto.getSpeciesId());
				System.out.println("找到物種為" + speciesRepository.findById(dto.getSpeciesId()));

				rescueCase.setSpecies(speciesRepository.findById(dto.getSpeciesId())
						.orElseThrow(() -> new RuntimeException("Species not found")));
				rescueCase.setFurColor(furColorRepository.findById(dto.getFurColorId())
						.orElseThrow(() -> new RuntimeException("FurColor not found")));
				rescueCase.setBreed(breedRepository.findById(dto.getBreedId())
						.orElseThrow(() -> new RuntimeException("Breed not found")));
				rescueCase.setCity(cityRepository.findById(dto.getCityId())
						.orElseThrow(() -> new RuntimeException("City not found")));
				rescueCase.setDistrictArea(districtAreaRepository.findById(dto.getDistrictAreaId())
						.orElseThrow(() -> new RuntimeException("DistrictArea not found")));
				rescueCase.setCaseState(caseStateRepository.findById(dto.getCaseStateId())
						.orElseThrow(() -> new RuntimeException("CaseState not found")));

				// 新增 canAffords 的處理
				List<CanAfford> canAffordEntities = dto.getCanAffords().stream()
						.map(canAffordDto -> canAffordRepository.findById(canAffordDto.getCanAffordId())
								.orElseThrow(() -> new RuntimeException(
										"CanAfford not found for ID: " + canAffordDto.getCanAffordId())))
						.toList();
				rescueCase.setCanAffords(canAffordEntities);

				// 新增 rescueDemands 的處理
				List<RescueDemand> rescueDemandEntities = dto.getRescueDemands().stream()
						.map(rescueDemandDto -> rescueDemandRepository.findById(rescueDemandDto.getRescueDemandId())
								.orElseThrow(() -> new RuntimeException(
										"RescueDemand not found for ID: " + rescueDemandDto.getRescueDemandId())))
						.toList();
				rescueCase.setRescueDemands(rescueDemandEntities);

				// 設定圖片關聯
				rescueCase.setCasePictures(casePictures);

				// 保存 RescueCase 到資料庫
				rescueCaseRepository.save(rescueCase);
			}
		}
	}

}
