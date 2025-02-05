package tw.com.ispan;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringBootConfig implements WebMvcConfigurer {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                                .allowedOrigins("http://localhost:5173")
                                .allowedMethods("GET", "POST", "PUT", "DELETE")
                                .allowedHeaders("*")
                                .allowCredentials(true);
        }

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/images/**")
                                .addResourceLocations("file:C:/meowWebsite/images/"); // ✅ 確保這裡是圖片存放的實際路徑
        }
}