package tw.com.ispan.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import jakarta.annotation.PostConstruct;
//指定不同repository使用的資料庫，記得yml要改手動配置，不然還是自動掃描配置
@Configuration
@EnableJpaRepositories(
		basePackages = {
				"tw.com.ispan.repository.admin",
				"tw.com.ispan.repository.pet",
				"tw.com.ispan.repository.shop"
				})
@EnableRedisRepositories(basePackages = "tw.com.ispan.repository.redis")
public class DataSourceConfig {
	
	
	@PostConstruct
    public void logRepositories() {
        System.out.println("JPA Repositories: admin, pet, shop");
        System.out.println("Redis Repositories: redis");
    }
}
