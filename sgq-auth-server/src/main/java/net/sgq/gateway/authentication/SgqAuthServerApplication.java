package net.sgq.gateway.authentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

@EnableAuthorizationServer
@SpringBootApplication
public class SgqAuthServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SgqAuthServerApplication.class, args);
	}

}
