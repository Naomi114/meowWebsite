package tw.com.ispan;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringBootConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        /* 將前後端頁面註冊後，CORS可以跨過SOP限制 */

        registry.addMapping("/**") // 允許所有路徑
                .allowedOrigins("http://localhost:5173") // 允許前端的請求
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true); // 允許帶認證（如 Cookies）
    }

    // registry.addMapping("/ajax/secure/login");
}
