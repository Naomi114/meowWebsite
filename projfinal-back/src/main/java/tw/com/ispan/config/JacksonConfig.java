// package tw.com.ispan.config;

// import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

// @Configuration
// public class JacksonConfig {

//     @Bean
//     public ObjectMapper objectMapper() {
//         // Register the Hibernate module with Jackson
//         Hibernate5Module hibernate5Module = new Hibernate5Module();
//         hibernate5Module.enable(Hibernate5Module.Feature.FORCE_LAZY_LOADING);

//         // Return a Jackson ObjectMapper with the Hibernate module registered
//         return Jackson2ObjectMapperBuilder.json()
//                 .modulesToInstall(hibernate5Module)
//                 .build();
//     }
// }
