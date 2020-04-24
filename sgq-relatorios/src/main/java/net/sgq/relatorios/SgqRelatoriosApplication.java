package net.sgq.relatorios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SgqRelatoriosApplication {

	public static void main(String[] args) {
		SpringApplication.run(SgqRelatoriosApplication.class, args);
	}

}
