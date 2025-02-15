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

 // 預定的名字
 private final String[] names = { "Bob", "John", "Alice", "Charlie", "David"
 };

 public loginData(MemberRepository memberRepository) {
 this.memberRepository = memberRepository;
 }

 @Override
 public void run(String... args) throws Exception {
 
 List<Integer> memberList = memberRepository.findMemberIdsInRange(1, 5);
 if (memberList.size() != 5) {
	 
		 // 創建五筆資料
	 for (int i = 0; i < 5; i++) {
	 Member member = new Member();
	
	 // 隨機選擇名字
	 String name = names[i];
	 String password = generatePassword(); // 生成隨機的大寫字母作為密碼
	 String email = generateEmail(name); // 根據名字生成電子郵件
	
	 // nickname 直接設置為 name
	 String nickname = name;
	
	 member.setNickName(nickname); // 設置 Nickname
	 member.setPassword(password); // 設置密碼
	 member.setName(name); // 設置名字
	 member.setEmail(email); // 設置電子郵件
	 member.setPhone(generateRandomPhone()); // 隨機生成電話號碼
	 member.setAddress("123 Lab St " + (i + 1)); // 隨機生成地址
	 member.setBirthday(LocalDate.of(2001, 7, 20)); // 固定生日
	 member.setCreateDate(LocalDateTime.now()); // 使用當前時間
	 member.setUpdateDate(LocalDateTime.now()); // 使用當前時間
	
	 // 保存資料到資料庫
	 memberRepository.save(member);
	 }
 }
	
	 System.out.println("五筆個性化資料已經插入到 Member 表格");
	 }
	
	 // 生成密碼：密碼為一個隨機的大寫字母
	 private String generatePassword() {
	 char letter = (char) ('A' + random.nextInt(26)); // 隨機選擇一個大寫字母
	 return String.valueOf(letter); // 密碼就是隨機的大寫字母
	 }
	
	 // 根據名字生成 email（例如: alice@lab.com）
	 private String generateEmail(String name) {
	 return name.toLowerCase() + "@lab.com"; // 以名字的小寫形式生成 email
	 }
	
	 // 隨機生成一個電話號碼
	 private String generateRandomPhone() {
	 return "09" + (10000000 + random.nextInt(10000000)); // 隨機生成台灣的手機號碼
	 }

 }
