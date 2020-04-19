package net.sgq.incidentes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SgqGestaoIncidenteApplication {

	public static void main(String[] args) {
		SpringApplication.run(SgqGestaoIncidenteApplication.class, args);
	}
	
}
