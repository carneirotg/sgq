package net.sgq.incidentes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableFeignClients
@EnableResourceServer
@EnableCaching
public class SgqGestaoIncidenteApplication {

	public static void main(String[] args) {
		SpringApplication.run(SgqGestaoIncidenteApplication.class, new String[] {});
	}
	
}
