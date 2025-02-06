package tw.com.ispan.interceptor;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tw.com.ispan.domain.admin.Member;
import tw.com.ispan.domain.shop.CartActionLog;
import tw.com.ispan.service.MemberService;
import tw.com.ispan.service.shop.CartActionLogService;

@Component
public class CartActionInterceptor implements HandlerInterceptor {
    @Autowired
    private CartActionLogService cartActionLogService;

    @Autowired
    private MemberService memberService; // ✅ 新增 MemberService 來查找會員

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestURI = request.getRequestURI();

        if (requestURI.startsWith("/pages/cart/list/")) {
            try {
                Long memberId = Long.parseLong(requestURI.split("/")[4]); // 解析 memberId
                Member member = memberService.findById(memberId); // ✅ 查找會員
                if (member == null) {
                    System.err.println("會員 ID " + memberId + " 不存在，無法記錄購物車行為");
                    return true;
                }

                CartActionLog log = new CartActionLog();
                log.setMember(member); // ✅ 設定 `Member` 物件
                log.setAction("查詢購物車");
                log.setTimestamp(LocalDateTime.now());

                // ✅ 儲存行為到資料庫
                cartActionLogService.saveLog(log);
            } catch (Exception e) {
                System.err.println("解析購物車查詢請求失敗: " + e.getMessage());
            }
        }
        return true;
    }
}
