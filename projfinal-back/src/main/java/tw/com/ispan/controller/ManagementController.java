package tw.com.ispan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tw.com.ispan.domain.admin.Member;
import tw.com.ispan.dto.MemberDto;
import tw.com.ispan.service.MemberService;

@RestController
@RequestMapping("/api/members")
public class ManagementController {

    @Autowired
    private MemberService memberService;

    // 獲取所有會員
    @GetMapping
    public List<Member> getMembers(@RequestParam(value = "search", required = false) String search) {
        if (search != null && !search.isEmpty()) {
            return memberService.searchMembers(search); // 根據搜尋條件過濾會員
        } else {
            return memberService.getAllMembers(); // 沒有搜尋條件則返回所有會員
        }
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable Integer memberId) {
        boolean isDeleted = memberService.deleteMemberById(memberId);
        if (isDeleted) {
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    @PostMapping
    public ResponseEntity<String> addMember(@RequestBody MemberDto memberDto) {

        if (memberDto.getBirthday() == null) {
            return ResponseEntity.badRequest().body("生日是必填項，不能為空");
        }
        memberService.addMember(memberDto);
        return ResponseEntity.ok("會員新增成功");
    }

    @PutMapping("/{memberId}")
    public ResponseEntity<String> updateMember(
            @PathVariable("memberId") Integer memberId,
            @RequestBody MemberDto memberDto) {
        try {
            // 調用服務層更新方法
            memberService.updateMember(memberId, memberDto);
            return ResponseEntity.ok("會員資料已成功更新");
        } catch (IllegalArgumentException e) {
            // 如果拋出會員不存在的異常
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("會員不存在：" + e.getMessage());
        } catch (Exception e) {
            // 捕捉其他異常
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("更新失敗：" + e.getMessage());
        }
    }
}
