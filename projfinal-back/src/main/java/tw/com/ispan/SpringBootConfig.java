package tw.com.ispan;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringBootConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        /* 將前後端頁面註冊後，CORS可以跨過SOP限制 */
        // registry.addMapping("/ajax/pages/products/**")
        // .allowedMethods("GET", "POST", "PUT", "DELETE");

        // registry.addMapping("/ajax/secure/login");
    }
}
