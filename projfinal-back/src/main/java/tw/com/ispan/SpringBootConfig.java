package tw.com.ispan;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringBootConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/cart/**") // Ensure this is the correct endpoint path for your cart
                .allowedOrigins("http://localhost:5173") // Allow frontend on port 5173 to access
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Allow necessary HTTP methods
                .allowedHeaders("*") // Allow all headers
                .allowCredentials(true); // Allow credentials (cookies, authentication headers)

        registry.addMapping("/products/**") // Mapping for product-related endpoints
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);

        registry.addMapping("/pages/cart/**") // Mapping for product-related endpoints
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
