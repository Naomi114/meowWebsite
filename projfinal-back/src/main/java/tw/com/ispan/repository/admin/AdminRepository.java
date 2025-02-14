package tw.com.ispan.repository.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import tw.com.ispan.domain.admin.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Optional<Admin> findByAdminName(String adminName);

    // 初始化資料避免一直重複(冠)
    // 由於僅提供內部資料初始化使用，因此不怕SQL injection
    @Query("SELECT a.id FROM Admin a WHERE a.id BETWEEN :start AND :end")
    List<Integer> findAdminIdsInRange(@Param("start") int start, @Param("end") int end);

    // @ManyToOne 刪除商品時，解除 Admin 與 Product 的關聯，但不刪除 Admin 本身
    // 取 @JoinTable name的表格名稱
    // 根據 productId，將 FK_adminId 設為 NULL，解除 Product 和 Admin 之間的關聯。
    @Modifying
    @Transactional
    @Query(value = "UPDATE product SET FK_adminId = NULL WHERE productId = :productId", nativeQuery = true)
    void removeAdminFromProduct(@Param("productId") Integer productId);

}
