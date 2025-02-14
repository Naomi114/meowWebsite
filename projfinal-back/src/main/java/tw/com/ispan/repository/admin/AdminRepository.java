package tw.com.ispan.repository.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tw.com.ispan.domain.admin.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Optional<Admin> findByAdminName(String adminName);
    
    
	//初始化資料避免一直重複(冠)
	//由於僅提供內部資料初始化使用，因此不怕SQL injection
	@Query("SELECT a.id FROM Admin a WHERE a.id BETWEEN :start AND :end")
	List<Integer> findAdminIdsInRange(@Param("start") int start, @Param("end") int end);
	
}
