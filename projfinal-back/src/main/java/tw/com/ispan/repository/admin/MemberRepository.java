package tw.com.ispan.repository.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.com.ispan.domain.admin.Member;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    Optional<Member> findByNickName(String nickName);

    Optional<Member> findByEmail(String email);

    List<Member> findByEmailContaining(String email);
    
    // NotificationService 查詢會員 (by Naomi)
    Optional<Member> findById(Long memberId);

    // by Naomi
    boolean existsByEmail(String email);

    // by Naomi
    boolean existsByNickName(String nickname);
}
