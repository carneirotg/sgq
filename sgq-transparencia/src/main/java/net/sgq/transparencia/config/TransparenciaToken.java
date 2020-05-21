package net.sgq.transparencia.config;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import net.sgq.transparencia.clientes.AuthServerClient;
import net.sgq.transparencia.clientes.to.AccessTokenTO;

@Component
public class TransparenciaToken {

	@Autowired
	private AuthServerClient client;
	
	@Value("${sgq.authorizationServer.username}")
	private String username;
	
	@Value("${sgq.authorizationServer.password}")
	private String password;
	
	private String tokenJWT = null;
	
	private Logger logger = org.slf4j.LoggerFactory.getLogger(TransparenciaToken.class);
	
	@PostConstruct
	@Scheduled(fixedDelay = 15000)
	public void initToken()  {
		try {
			logger.debug("Atualizando token JWT Interno");
			recuperaToken();
		} catch (Exception e) {
			logger.error("Erro recuperando token: {}", e.getMessage(), e);
		}
	}
	
	public String getJwtToken() {
		return tokenJWT;
	}
	
	private void recuperaToken() {
		AccessTokenTO tokenTO = client.login(params());
		this.tokenJWT = tokenTO.getAccessToken();
	}

	private Map<String, ?> params() {
		Map<String, String> p = new HashMap<>();
		p.put("grant_type", "password");
		p.put("username", this.username);
		p.put("password", this.password);
		p.put("scope", "any");

		return p;
	}

}
