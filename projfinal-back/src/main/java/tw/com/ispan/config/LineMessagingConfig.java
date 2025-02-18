package tw.com.ispan.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.linecorp.bot.client.LineMessagingClient;

@Configuration
public class LineMessagingConfig {

	// message API的Channel Access Token
	@Value("${Channel.Access.Token}")
	private String channelAccessToken;

	// LineMessagingClient 通過 Spring 容器管理，並在配置類中初始化
	@Bean
	public LineMessagingClient lineMessagingClient() {
		String channelToken = channelAccessToken;
		return LineMessagingClient.builder(channelToken).build();
	}
}
