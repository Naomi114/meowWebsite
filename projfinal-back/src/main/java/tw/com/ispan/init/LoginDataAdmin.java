package tw.com.ispan.init;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import tw.com.ispan.domain.admin.Admin;
import tw.com.ispan.repository.admin.AdminRepository;

@Component
public class LoginDataAdmin implements CommandLineRunner {

    private final AdminRepository adminRepository;

    public LoginDataAdmin(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 檢查 Admin 資料表是否為空
        if (adminRepository.count() == 0) {
            // 如果資料表為空，則插入資料
            // 創建一個新的 Admin 實例
            Admin admin = new Admin();
            admin.setAdminName("admin"); // 設置 adminName
            admin.setPassword("AAA"); // 設置 password
            admin.setCreateDate(LocalDateTime.now()); // 設置創建時間
            admin.setUpdateDate(LocalDateTime.now()); // 設置更新時間

            // 儲存到資料庫
            adminRepository.save(admin);

            System.out.println("資料已成功插入到 Admin 表格");
        } else {

        }
    }
}
