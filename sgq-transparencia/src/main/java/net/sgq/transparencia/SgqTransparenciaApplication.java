package net.sgq.transparencia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
@EnableResourceServer
@EnableCaching
public class SgqTransparenciaApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(SgqTransparenciaApplication.class, new String[] {});
	}
	
}
