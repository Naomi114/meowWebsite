package tw.com.ispan.service.line;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Button;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;

import jakarta.validation.constraints.AssertFalse.List;
import tw.com.ispan.domain.admin.Member;
import tw.com.ispan.domain.pet.LineTemporaryBinding;
import tw.com.ispan.repository.admin.MemberRepository;
import tw.com.ispan.repository.pet.LineTemporaryBindingRepository;

@Service
@Transactional
public class LineBindingService {

	//怎麼拿到這個用戶產生的token??
	
    @Autowired
    private StateRedisService stateRedisService; 
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private LineTemporaryBindingRepository lineTemporaryBindingRepository;
    @Autowired
    private LineNotificationService lineNotificationService;

	
    //確認此memberid有無綁定一個lineid
    public boolean isLineBound(Integer memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("會員不存在"));
        return member.getLineId() != null;
    }



	
	//創建 Flex Message，包含綁定按鈕 
// 	private FlexMessage createBindingFlexMessage(Integer memberId) {
//         Bubble bubble = Bubble.builder()
//             .body(Box.builder()
//                 .layout(FlexLayout.VERTICAL)
//                 .contents(List.of(
//                     Text.builder().text("請點擊下方按鈕完成綁定").build(),
//                     Button.builder()
//                         .action(PostbackAction.builder()
//                             .label("綁定")
//                             .data("action=bind&memberId=" + memberId)
//                             .build())
//                         .build()
//                 ))
//                 .build())
//             .build();

//         return new FlexMessage("綁定確認", bubble);
//     }
	
	
// 	//使用 LINE Messaging API 發送綁定確認消息
// 	private void sendBindingMessageToUser(String memberId, String lineId) {
   
//         // 構建按鈕消息
//         FlexMessage flexMessage = createBindingFlexMessage(memberId);

//         // 使用 LINE Messaging API 發送消息
//         lineMessagingClient.pushMessage(new PushMessage(lineId, flexMessage));
//         System.out.println("綁定確認消息已發送");
    
// }
	
	
// 	//產生用戶lineId的綁定連結
// 	public String generateBindingLinkForLineId(String lineId) {
// 	    // 使用 UUID 生成唯一 Token
// 	    String bindingToken = UUID.randomUUID().toString();

// 	    // 將 token 和 LINE ID 暫存至數據庫
// 	    LineTemporaryBinding lineTemporaryBinding = new LineTemporaryBinding();
// 	    lineTemporaryBinding.setLineId(lineId);
// 	    lineTemporaryBinding.setBindingToken(bindingToken);
// 	    lineTemporaryBinding.setExpiryTime(LocalDateTime.now().plusMinutes(10));
// 	    lineTemporaryBindingRepository.save(lineTemporaryBinding);

// 	    return bindingToken;
// 	}
	
	
	
	
	
// 	// 當用戶發起綁定請求，透過此方法產生唯一綁定token鏈結，發送回去給用戶點選發送request，將自己lineid和token傳進controller
// //	public String generateBindingLink(Integer memberId) {
// //		// 使用 UUID 生成唯一 Token
// //		String bindingToken = UUID.randomUUID().toString();
// //
// //		// 保存到資料庫（綁定 token 和會員 ID 的映射）
// //		Optional<Member> memberOpt = memberRepository.findById(memberId);
// //		if (memberOpt.isPresent()) {
// //			Member member = memberOpt.get();
// //
// //			// 設置 Token 和過期時間
// //			member.setBindingToken(bindingToken);
// //			member.setBindingTokenExpiry(LocalDateTime.now().plusMinutes(10)); // Token 過期時間為10分鐘
// //
// //			// 保存到數據庫
// //			memberRepository.save(member);
// //		} else {
// //			throw new IllegalArgumentException("會員不存在");
// //		}
// //
// //		// 返回綁定鏈接
// //		return "https://586f-1-160-6-252.ngrok-free.app/line/bind?token=" + bindingToken; // 典型RESTful URL
// //	}

// 	// 完成綁定// 查找對應的會員並驗證token
// 	public ResponseEntity<String> completeBinding(String token, String lineUserId) {
		
// 		// 查找對應的會員並驗證token
// 		Optional<Member> memberOpt = memberRepository.findByBindingToken(token);
// 		if (memberOpt.isEmpty()) {
// 			return ResponseEntity.badRequest().body("無效的綁定請求！"); // 此Token不存在
// 		}

// 		Member member = memberOpt.get();

// 		// 驗證 Token 是否過期（可選）
// 		if (member.getBindingTokenExpiry().isBefore(LocalDateTime.now())) {
// 			return ResponseEntity.badRequest().body("綁定已過期！請於10分鐘內進行操作");
// 		}

// 		// 綁定 LINE User ID
// 		member.setUserLineId(lineUserId);
// 		member.setBindingToken(null); // 清除 Token，防止重複綁定
// 		member.setBindingTokenExpiry(null);
// 		memberRepository.save(member);

// 		return ResponseEntity.ok("綁定成功！");
// 	}
}
