package tw.com.ispan.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.com.ispan.domain.admin.Member;
import tw.com.ispan.dto.MemberDto;
import tw.com.ispan.dto.PasswordDto;
import tw.com.ispan.repository.admin.MemberRepository;

@Service
@Transactional
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    // 登入
    public Member login(String email, String password) {
        if (email != null && email.length() != 0 && password != null && password.length() != 0) {
            Optional<Member> optional = memberRepository.findByEmail(email); // 根據 email 查詢
            if (optional.isPresent()) {
                Member bean = optional.get();
                System.out.println("找到的用戶：" + bean.getEmail()); // 檢查查詢結果
                String storedPassword = bean.getPassword(); // 儲存的密碼
                if (storedPassword.equals(password)) { // 直接比對字串
                    return bean; // 密碼正確，返回用戶資料
                }
            } else {
                System.out.println("未找到該用戶");
            }
        }
        return null; // 若無效的用戶或密碼錯誤，返回 null
    }

    // 註冊
    public void addMember2(MemberDto memberDto) {
        // 先進行會員資料新增
        Member member = mapToEntity(memberDto);

        // 直接使用明文密碼，不加密
        member.setPassword(memberDto.getPassword());

        // 設置 createDate 和 updateDate
        if (member.getCreateDate() == null) {
            member.setCreateDate(LocalDateTime.now());
        }
        member.setUpdateDate(LocalDateTime.now());
        // 保存會員資料
        memberRepository.save(member);
        System.out.println("會員註冊成功，開始自動登入...");

        // 加入延遲，確保資料庫有時間寫入資料
        try {
            Thread.sleep(1000); // 延遲 1秒，確保資料庫寫入完成
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 檢查資料庫中的資料是否存在
        Optional<Member> savedMember = memberRepository.findByEmail(member.getEmail());
        if (savedMember.isPresent()) {
            System.out.println("資料庫中找到該用戶，開始登入...");
        } else {
            System.out.println("資料庫中未找到該用戶，註冊資料保存失敗！");
            return; // 如果資料庫中沒有這個會員，退出
        }
        // 註冊後自動登入
        Member loggedInMember = login(member.getEmail(), memberDto.getPassword());

        if (loggedInMember != null) {
            System.out.println("自動登入成功！");
        } else {
            System.out.println("自動登入失敗！");
        }
    }

    // 新增
    public void addMember(MemberDto memberDto) {
        Member member = mapToEntity(memberDto);

        // 設置 createDate 和 updateDate
        if (member.getCreateDate() == null) {
            member.setCreateDate(LocalDateTime.now());
        }
        member.setUpdateDate(LocalDateTime.now());
        memberRepository.save(member);
    }

    // 修改密碼
    public boolean changePassword(PasswordDto passwordDto) {
        // 1. 檢查用戶是否存在
        Optional<Member> optionalMember = memberRepository.findByEmail(passwordDto.getEmail()); // 根據 email 查詢
        if (optionalMember.isEmpty()) {
            // 用戶不存在
            return false;
        }
        Member member = optionalMember.get();
        // 2. 驗證舊密碼是否正確
        if (!member.getPassword().equals(passwordDto.getOldPassword())) {
            // 舊密碼不正確
            return false;
        }
        // 3. 驗證新密碼是否和舊密碼相同
        if (passwordDto.getOldPassword().equals(passwordDto.getNewPassword())) {
            // 新密碼不能和舊密碼相同
            return false;
        }
        // 4. 更新密碼
        member.setPassword(passwordDto.getNewPassword());
        memberRepository.save(member);
        return true; // 密碼更新成功
    }

    // 查詢
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    // 查詢
    public List<Member> searchMembers(String query) {
        return memberRepository.findByEmailContaining(query);
    }

    // 刪除
    public boolean deleteMemberById(Integer memberId) {
        if (memberRepository.existsById(memberId)) { // 確認會員是否存在
            memberRepository.deleteById(memberId); // 刪除會員
            return true; // 刪除成功
        } else {
            return false; // 未找到會員
        }
    }

    // 修改會員資料
    public void updateMember(Integer memberId, MemberDto memberDto) {
        // 查詢會員是否存在
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    // 添加日誌
                    System.out.println("會員 ID " + memberId + " 不存在");
                    return new IllegalArgumentException("會員 ID " + memberId + " 不存在");
                });
        // 日誌：會員存在，開始更新
        System.out.println("找到會員 ID " + memberId + "，開始更新");
        // 更新會員資料
        member.setNickName(memberDto.getNickName());
        member.setPassword(memberDto.getPassword());
        member.setName(memberDto.getName());
        member.setEmail(memberDto.getEmail());
        member.setPhone(memberDto.getPhone());
        member.setAddress(memberDto.getAddress());
        member.setBirthday(memberDto.getBirthday());

        // 直接將當前日期時間設定為更新時間
        member.setUpdateDate(LocalDateTime.now()); // 使用當前時間作為 updateDate

        // 儲存更新後的資料
        memberRepository.save(member);
        System.out.println("會員 ID " + memberId + " 資料更新完成");
    }

    private Member mapToEntity(MemberDto dto) {
        Member member = new Member();
        member.setNickName(dto.getNickName());
        member.setPassword(dto.getPassword());
        member.setName(dto.getName());
        member.setEmail(dto.getEmail());
        member.setPhone(dto.getPhone());
        member.setAddress(dto.getAddress());
        member.setBirthday(dto.getBirthday());
        member.setCreateDate(dto.getCreateDate());
        // 添加 updateDate 映射
        return member;
    }

    // Entity 轉換為 DTO
    private MemberDto mapToDto(Member member) {
        return new MemberDto(
                member.getMemberId(),
                member.getNickName(),
                member.getPassword(),
                member.getName(),
                member.getEmail(),
                member.getPhone(),
                member.getAddress(),
                member.getBirthday(),
                member.getCreateDate()

        );
    }

    // 購物車操作紀錄會用到 (by Naomi)
    public Member findById(Long memberId) {
        return memberRepository.findById(memberId).orElse(null);
    }
}
