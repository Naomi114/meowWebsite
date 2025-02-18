package tw.com.ispan.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class cors implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173") // 允許的前端源
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 支持 OPTIONS 預檢請求
                .allowedHeaders("Authorization", "*") // 允許 Authorization 標頭
                .allowCredentials(true) // 如果需要傳遞 cookie 或其他認證信息
                .maxAge(3600); // 設定 CORS 預檢請求的緩存時間
    }

    @Bean
    public org.springframework.web.servlet.config.annotation.WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:5173") // 允許的前端源
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("Authorization", "Content-Type")
                        .allowCredentials(true); // 允許攜帶憑證
            }
        };
    }
}