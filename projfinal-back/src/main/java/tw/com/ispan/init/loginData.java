package tw.com.ispan.init;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import tw.com.ispan.domain.admin.Member;
import tw.com.ispan.repository.admin.MemberRepository;

@Component
public class loginData implements CommandLineRunner {

    private final MemberRepository memberRepository;

    public loginData(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Member member = new Member();
        member.setNickName("admin");
        member.setPassword("0x41"); // 密碼以十六進制存儲
        member.setName("Alex");
        member.setEmail("alex@lab.com");
        member.setPhone("1234567890");
        member.setAddress("123 Lab St");
        member.setBirthday(LocalDate.of(2001, 7, 20)); // 使用 LocalDate
        member.setCreateDate(LocalDateTime.now()); // 使用 LocalDateTime
        member.setUpdateDate(LocalDateTime.now()); // 使用 LocalDateTime

        memberRepository.save(member);
        System.out.println("資料已經插入到 Member 表格");
    }
}
