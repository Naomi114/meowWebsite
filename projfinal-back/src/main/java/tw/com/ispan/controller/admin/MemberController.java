package tw.com.ispan.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tw.com.ispan.domain.admin.Member;
import tw.com.ispan.dto.admin.MemberDto;
import tw.com.ispan.dto.admin.MemberProfileDto;
import tw.com.ispan.dto.admin.PasswordDto;
import tw.com.ispan.service.MemberService;

@RestController
@RequestMapping("/api/register")
public class MemberController {

    @Autowired
    private MemberService memberService;

    // 註冊
    @PostMapping
    public ResponseEntity<String> addMember2(@RequestBody MemberDto memberDto) {

        if (memberDto.getBirthday() == null) {
            return ResponseEntity.badRequest().body("生日是必填項，不能為空");
        }
        memberService.addMember2(memberDto);
        return ResponseEntity.ok("會員新增成功");
    }

    // 改密碼
    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody PasswordDto passwordDto) {
        try {
            // 呼叫 service 層執行密碼更新邏輯
            boolean isChanged = memberService.changePassword(passwordDto);

            if (isChanged) {
                return ResponseEntity.ok("Password changed successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid username or password, or new password cannot be the same as the old one.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + e.getMessage());
        }
    }

    // 冠，返回個人頁面資訊
    @GetMapping("/{memberId}/profile")
    public ResponseEntity<MemberProfileDto> getMemberProfile(@PathVariable Integer memberId) {
        Member member = memberService.getMemberById(memberId);
        if (member == null) {
            return ResponseEntity.notFound().build();
        }
        MemberProfileDto dto = new MemberProfileDto(member.getLineName(), member.getLinePicture(), member.getEmail());
        return ResponseEntity.ok(dto);
    }

}
