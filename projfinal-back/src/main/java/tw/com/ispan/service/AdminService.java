package tw.com.ispan.service;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.com.ispan.domain.admin.Admin;
import tw.com.ispan.repository.admin.AdminRepository;

@Service
@Transactional
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;

    public Admin login(String username, String password) {
        if (username != null && username.length() != 0 &&
                password != null && password.length() != 0) {
            Optional<Admin> optional = adminRepository.findByAdminName(username);
            if (optional.isPresent()) {
                Admin bean = optional.get();

                System.out.println("找到的用戶：" + bean.getAdminName()); // 檢查查詢結果

                String storedPassword = bean.getPassword(); // 儲存的密碼 (假設是字符串)

                if (storedPassword.equals(password)) { // 比對字串
                    return bean; // 密碼正確，返回用戶資料
                }
            } else {
                System.out.println("未找到該用戶");
            }
        }
        return null; // 若無效的用戶或密碼錯誤，返回 null
    }
}