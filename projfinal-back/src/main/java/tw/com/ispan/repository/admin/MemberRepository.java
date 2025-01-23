package tw.com.ispan.repository.admin;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tw.com.ispan.domain.admin.Member;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    
	Optional<Member> findByNickName(String nickName);
	 
	// Spring Data JPA 的自定義查詢方法  SELECT * FROM bindingToken WHERE token = ?;
	Optional<Member> findByBindingToken(String bindingToken);
	
	// SELECT userLineId FROM member WHERE memberId = ?;
	String findUserLineIdByMemberId(Integer memberId);
	
	//默認情況下Spring Data JPA 的 @Query 方法僅用於查詢SELECT，當執行非查詢操作（如 INSERT、UPDATE、DELETE）時，必須添加 @Modifying
	//@Query定義自定義的 JPQL（Java Persistence Query Language）語句，操作的是 JPA 實體類，而不是直接操作數據庫表
	@Modifying  
	@Query("UPDATE Member m SET m.userLineId = :lineId WHERE m.id = :memberId")
	void bindLineIdandMemberId(@Param("memberId") Integer memberId, @Param("lineId") String lineId);
	
	
	// 檢查是否有該 LINE ID
    boolean existsByLineId(String lineId);
}