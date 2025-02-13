package tw.com.ispan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import tw.com.ispan.config.FileStorageProperties;

@SpringBootApplication
@EnableConfigurationProperties(FileStorageProperties.class)
public class ProjfinalApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjfinalApplication.class, args);
	}

}
