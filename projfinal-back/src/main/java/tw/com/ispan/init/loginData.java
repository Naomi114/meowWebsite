package tw.com.ispan.init;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import tw.com.ispan.domain.admin.Member;
import tw.com.ispan.repository.admin.MemberRepository;

@Component
@Order(1) // 最先執行
public class loginData implements CommandLineRunner {

	private final MemberRepository memberRepository;
	private final Random random = new Random();

	// 預定的 15 個名字
	private final String[] names = {
			"Bob", "John", "Alice", "Charlie", "David",
			"Emma", "Frank", "Grace", "Henry", "Ivy",
			"Jack", "Kevin", "Lily", "Mason", "Nora"
	};

	// 15 個不同的台灣地址
	private final String[] addresses = {
			"台北市 大安區 忠孝東路四段 100 號",
			"新北市 板橋區 文化路一段 200 號",
			"桃園市 中壢區 中山東路二段 300 號",
			"台中市 西屯區 台灣大道三段 400 號",
			"台南市 中西區 健康路一段 500 號",
			"高雄市 左營區 博愛二路 600 號",
			"基隆市 仁愛區 愛三路 700 號",
			"新竹市 東區 光復路二段 800 號",
			"苗栗縣 竹南鎮 中正路 900 號",
			"彰化縣 員林市 中山路二段 1000 號",
			"南投縣 南投市 南崗一路 1100 號",
			"雲林縣 斗六市 大同路 1200 號",
			"嘉義市 東區 民生南路 1300 號",
			"屏東縣 屏東市 建國路 1400 號",
			"宜蘭縣 羅東鎮 中正北路 1500 號"
	};

	public loginData(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	@Override
	public void run(String... args) throws Exception {
		// 取得現有的會員數量
		List<Integer> memberList = memberRepository.findMemberIdsInRange(1, 15);
		if (memberList.size() != 15) {

			// 創建 15 筆會員資料
			for (int i = 0; i < 15; i++) {
				Member member = new Member();

				// 使用預設名字
				String name = names[i];
				String password = name + "123"; // 設置密碼為「名字 + 123」
				String email = generateEmail(name); // 生成 email

				member.setNickName(name); // 設置 Nickname
				member.setPassword(password); // 設置密碼
				member.setName(name); // 設置名字
				member.setEmail(email); // 設置電子郵件
				member.setPhone(generateRandomPhone()); // 生成隨機電話號碼
				member.setAddress(addresses[i]); // 設置固定的台灣地址
				member.setBirthday(LocalDate.of(2001, 7, 20)); // 固定生日
				member.setCreateDate(LocalDateTime.now()); // 使用當前時間
				member.setUpdateDate(LocalDateTime.now()); // 使用當前時間

				// 保存到資料庫
				memberRepository.save(member);
			}
		}

		System.out.println("15 筆會員資料已經插入到 Member 表格");
	}

	// 根據名字生成 email（例如: alice@lab.com）
	private String generateEmail(String name) {
		return name.toLowerCase() + "@lab.com"; // 轉換為小寫
	}

	// 隨機生成台灣手機號碼
	private String generateRandomPhone() {
		return "09" + (10000000 + random.nextInt(10000000)); // 生成隨機手機號
	}
}
