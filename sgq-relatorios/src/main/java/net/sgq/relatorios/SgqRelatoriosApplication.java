package net.sgq.relatorios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication(scanBasePackages = "net.sgq")
@EnableFeignClients
@EnableResourceServer
public class SgqRelatoriosApplication {

	public static void main(String[] args) {
		SpringApplication.run(SgqRelatoriosApplication.class, new String[] {});
	}

}
