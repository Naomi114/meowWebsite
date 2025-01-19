package tw.com.ispan.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import tw.com.ispan.domain.admin.Member;

public interface MemberRepository extends JpaRepository<Member, Integer>, JpaSpecificationExecutor<Member> {

}
