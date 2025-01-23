package tw.com.ispan.repository.admin;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.ispan.domain.admin.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Optional<Admin> findByAdminName(String adminName);
}