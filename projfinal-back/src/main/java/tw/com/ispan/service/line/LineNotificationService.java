package tw.com.ispan.service.line;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.message.FlexMessage;   //用來建立 Flex 訊息
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.flex.component.Box;  //Box 容器（用來放入文字、圖片等）
import com.linecorp.bot.model.message.flex.component.Button; //按鈕元件
import com.linecorp.bot.model.message.flex.component.Text;  //文字元件
import com.linecorp.bot.model.message.flex.component.Text.TextWeight;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.container.FlexContainer;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;  //設定 FlexBox 排列方式

import tw.com.ispan.repository.admin.MemberRepository;

//此為操控平台LINE商家發送消息功能
@Service
@Transactional
public class LineNotificationService {

	//跳轉回前端案件頁面的網址
	
	
	// lineMessagingClient專門用於通過 LINE Messaging API 與 LINE 平台進行通信，可藉此發訊息給用戶
	// 通過構造方法@RequiredArgsConstructor注入 失敗!! 改手動寫建構子
	@Autowired
	private final LineMessagingClient lineMessagingClient;
	@Autowired
	private MemberRepository memberRepository;

	public LineNotificationService(LineMessagingClient lineMessagingClient) {
		this.lineMessagingClient = lineMessagingClient;
	}

	// 此方法用來傳送通知
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

	public void sendFlexNotification(String userLineId, String title, String content, String caseUrl) {
	    // 建立標題
	    Text titleText = Text.builder()
	            .text(title)
	            .weight(TextWeight.BOLD)  // 設定粗體字
	            .size("xl")  // 設定標題大小
	            .build();

	    // 建立內容
	    Text contentText = Text.builder()
	            .text(content)
	            .wrap(true)  // 允許換行
	            .margin("md")  // 與標題的間距
	            .size("md")
	            .build();

	    // 建立按鈕
	    // 這裡的url需要是有效的網址
	    Button caseButton = Button.builder()
	            .action(new URIAction ("查看案件", URI.create(caseUrl), null))   //參數分別是按鈕顯示文字、開啟網址(類別須為Uri)、可選替代網址(選填)
	            .style(Button.ButtonStyle.PRIMARY)  // 按鈕樣式
	            .build();

	    // 將標題、內容、按鈕放入 Box (垂直排列)
	    Box bodyBox = Box.builder()
	            .layout(FlexLayout.VERTICAL)
	            .contents(List.of(titleText, contentText, caseButton))  // 依序顯示
	            .build();

	    // 建立 Flex Bubble 容器
	    FlexContainer bubble = Bubble.builder()
	            .body(bodyBox)  // 設定內容
	            .build();

	    // 建立 Flex Message
	    FlexMessage flexMessage = new FlexMessage("案件變更通知", bubble);

	    // 發送訊息，並確保等待回應
	    PushMessage pushMessage = new PushMessage(userLineId, flexMessage);
	    CompletableFuture<Void> future = lineMessagingClient.pushMessage(pushMessage)
	            .thenAccept(response -> System.out.println("Flex Message sent successfully"))
	            .exceptionally(throwable -> {
	                System.err.println("Failed to send Flex Message: " + throwable.getMessage());
	                return null;
	            });

	    // 等待 Flex Message 發送完成（選擇性）
	    future.join();
	}


}
