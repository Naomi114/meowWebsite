package tw.com.ispan.projfinal_back.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.com.ispan.projfinal_back.domain.admin.Member;

public interface MemberRepository extends JpaRepository<Member, Integer> {

}
