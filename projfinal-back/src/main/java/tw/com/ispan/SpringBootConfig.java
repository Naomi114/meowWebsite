package tw.com.ispan;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringBootConfig implements WebMvcConfigurer {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/cart/**")
                                .allowedOrigins("http://localhost:5173") // Allow frontend at localhost:5173
                                .allowedMethods("GET", "POST", "PUT", "DELETE")
                                .allowedHeaders("*")
                                .allowCredentials(true);

                registry.addMapping("/products/**")
                                .allowedOrigins("http://localhost:5173") // Allow frontend at localhost:5173
                                .allowedMethods("GET", "POST", "PUT", "DELETE")
                                .allowedHeaders("*")
                                .allowCredentials(true);

                registry.addMapping("/products/**")
                                .allowedOrigins("http://localhost:5173") // Allow frontend at localhost:5173
                                .allowedMethods("GET", "POST", "PUT", "DELETE")
                                .allowedHeaders("*")
                                .allowCredentials(true);

                registry.addMapping("/pages/cart/**")
                                .allowedOrigins("http://localhost:5173")
                                .allowedMethods("GET", "POST", "PUT", "DELETE")
                                .allowedHeaders("*")
                                .allowCredentials(true);

                registry.addMapping("/pages/ecpay/send/**")
                                .allowedOrigins("http://localhost:5173") // Allow frontend at localhost:5173
                                .allowedMethods("GET", "POST", "PUT", "DELETE")
                                .allowedHeaders("*")
                                .allowCredentials(true);
        }
}
