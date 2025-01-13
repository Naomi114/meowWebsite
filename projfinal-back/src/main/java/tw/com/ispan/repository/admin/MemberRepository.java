package tw.com.ispan.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.ispan.domain.admin.Member;

public interface MemberRepository extends JpaRepository<Member, Integer> {

}
