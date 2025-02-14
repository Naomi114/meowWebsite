
 package tw.com.ispan.init;

 import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import tw.com.ispan.domain.admin.Admin;
import tw.com.ispan.domain.pet.Species;
import tw.com.ispan.repository.admin.AdminRepository;

 @Component
 @Order(2) // 第二順位執行
 public class LoginDataAdmin implements CommandLineRunner {

 @Autowired
 private final AdminRepository adminRepository;

 public LoginDataAdmin(AdminRepository adminRepository) {
 this.adminRepository = adminRepository;
 }

 @Override
 public void run(String... args) throws Exception {

	 if (!adminRepository.existsById(1)) {
		
	 
		 // 創建一個新的 Admin 實例
		 Admin admin = new Admin();
		 admin.setAdminName("admin"); // 設置 adminName
		 admin.setPassword("AAA"); // 設置 password
		 admin.setCreateDate(LocalDateTime.now()); // 設置創建時間
		 admin.setUpdateDate(LocalDateTime.now()); // 設置更新時間
		
		 // 儲存到資料庫
		 adminRepository.save(admin);
	 }

	 System.out.println("資料已成功插入到 Admin 表格");
 	}
 }