package tw.com.ispan.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 全局 CORS 配置
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 對所有路徑生效
                .allowedOrigins("http://localhost:5173") // 允許的前端 URL
                .allowedMethods("*") // 允許的請求方法
                .allowedHeaders("*") // 允許的請求頭
                .allowCredentials(true); // 是否允許攜帶憑證
    }

    // 映射靜態資源，讓前端可透過url訪問後端專案中資源(如圖片)
    // 等同將http://localhost:8080/upload映射C:/upload/這個路徑，後面再加自己設的其他路徑如/final/pet/images/圖檔名
    // dto中要返回前端可存取的 URL，而不是後端的內部文件路徑，所以獲取後端儲存的路徑後要修改
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:C:/upload/");
    }
}