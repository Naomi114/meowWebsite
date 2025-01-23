package tw.com.ispan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class WebMvcConfigurer {
	
	
	@Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // 匹配所有路徑
                        .allowedOrigins("http://localhost:5173") // 允許的來源
                        .allowedMethods("GET", "POST", "PUT", "DELETE") // 允許的 HTTP 方法
                        .allowedHeaders("*") // 允許的標頭
                        .allowCredentials(true); // 是否允許攜帶憑據（如 Cookie）
            }
        };
    }

	public void addCorsMappings(CorsRegistry registry) {
		// TODO Auto-generated method stub
		
	}
}
