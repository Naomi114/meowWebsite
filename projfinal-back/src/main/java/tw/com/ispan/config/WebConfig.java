// package tw.com.ispan.config;

// import org.springframework.context.annotation.Configuration;
// import
// org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// // 建立spring boot靜態資源，讓前端可以獲取http://localhost:8080/images/* 中的靜態圖片資源
// @Configuration
// public class WebConfig implements WebMvcConfigurer {

// @Override
// public void addResourceHandlers(ResourceHandlerRegistry registry) {
// registry.addResourceHandler("/images/**")
// .addResourceLocations("file:C:/meowWebsite/images/"); // ✅ 確保這裡是圖片存放的實際路徑
// }
// }
