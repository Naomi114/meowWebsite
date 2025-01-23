package tw.com.ispan.service.line;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.flex.container.FlexContainer;

import tw.com.ispan.repository.admin.MemberRepository;

//此為發送消息功能
@Service
@Transactional
public class LineNotificationService {

	// lineMessagingClient專門用於通過 LINE Messaging API 與 LINE 平台進行通信，可藉此發訊息給用戶
	// 通過構造方法@RequiredArgsConstructor注入 失敗!! 改手動寫建構子
	@Autowired
	private final LineMessagingClient lineMessagingClient;
	@Autowired
	private MemberRepository memberRepository;

	public LineNotificationService(LineMessagingClient lineMessagingClient) {
		this.lineMessagingClient = lineMessagingClient;
	}

	
	// 此方法用來傳送協尋通知
	public void sendNotification(String userLineId, String message) {
		TextMessage textMessage = new TextMessage(message);
		PushMessage pushMessage = new PushMessage(userLineId, textMessage);

		CompletableFuture<Void> future = lineMessagingClient.pushMessage(pushMessage)
				.thenAccept(response -> System.out.println("Message sent successfully")).exceptionally(throwable -> {
					System.err.println("Failed to send message: " + throwable.getMessage());
					return null;
				});

		// 等待消息發送完成（選擇性）
		future.join();
	}
	
	
	// 傳送用戶綁定鏈結的方法
	public void sendBindingMessage(String LineId, String bindingLink) {

		// 創建消息內容
		TextMessage textMessage = new TextMessage("點擊此連結以完成綁定: " + bindingLink);

		// 發送消息
		PushMessage pushMessage = new PushMessage(LineId, textMessage);
		lineMessagingClient.pushMessage(pushMessage).thenAccept(response -> System.out.println("綁定消息已成功發送"))
				.exceptionally(throwable -> {
					System.err.println("綁定消息發送失敗: " + throwable.getMessage());
					return null;
				});
	}

	
}

