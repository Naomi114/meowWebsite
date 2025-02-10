package tw.com.ispan.config;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tw.com.ispan.interceptor.CartActionInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private CartActionInterceptor cartActionInterceptor;

    @Autowired
    private AuthenticationInterceptor authenticationInterceptor; // 注入 AuthenticationInterceptor

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

        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:C:/meowWebsite/images/"); // ✅ 確保這裡是圖片存放的實際路徑
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(cartActionInterceptor)
                .addPathPatterns("/pages/cart/list/**"); // ✅ 指定攔截購物車 API

        // 註冊身份驗證攔截器
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns("/api/**") // 設定攔截 /api/** 路徑的所有請求
                .excludePathPatterns("/api/login", "/api/register"); // 排除不需要身份驗證的路徑，例如登入和註冊
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter()); // ✅ 確保 JSON 轉換器可用
    }

    @Bean
    public Filter coepCoopFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                    FilterChain filterChain)
                    throws ServletException, IOException {
                // 設定 Cross-Origin-Opener-Policy 和 Cross-Origin-Embedder-Policy 標頭
                response.setHeader("Cross-Origin-Opener-Policy", "unsafe-none");
                response.setHeader("Cross-Origin-Embedder-Policy", "unsafe-none");
                // 繼續執行後續的過濾器
                filterChain.doFilter(request, response);
            }
        };
    }
}
