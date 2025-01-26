package tw.com.ispan.repository.admin;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.ispan.domain.admin.Member;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    // Find member by memberId
    Optional<Member> findById(Member memberId);
}
