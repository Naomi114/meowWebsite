package tw.com.ispan.repository.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tw.com.ispan.domain.admin.Member;
import tw.com.ispan.dto.MemberDto;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    Optional<Member> findByEmail(String email);

    List<Member> findByEmailContaining(String email);


	//以下為冠儒增加的
	Optional<Member> findByNickName(String nickName);

	// 默認情況下Spring Data JPA 的 @Query 方法僅用於查詢SELECT，當執行非查詢操作（如
	// INSERT、UPDATE、DELETE）時，必須添加 @Modifying
	// @Query定義自定義的 JPQL（Java Persistence Query Language）語句，操作的是 JPA 實體類，而不是直接操作數據庫表
	@Modifying
	@Query("UPDATE Member m SET m.lineId = :lineId WHERE m.memberId = :memberId")
	void bindLineIdandMemberId(@Param("memberId") Integer memberId, @Param("lineId") String lineId);

	// 檢查是否有該 LINE ID
	boolean existsByLineId(String lineId);

	// 檢查是否有追蹤商家帳號
	Boolean existsByMemberIdAndFollowed(Integer memberId, boolean followed);

	// 找到特定lineid的追蹤狀態改為true
	@Modifying
	@Query("UPDATE Member m SET m.followed = true WHERE m.lineId = :lineId")
	void updateFollowed(@Param("lineId") String lineId);

}
