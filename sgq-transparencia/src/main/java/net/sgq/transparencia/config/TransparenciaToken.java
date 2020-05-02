package net.sgq.transparencia.config;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class TransparenciaToken {

	private static final String ACCESS_TOKEN = "access_token";

	@Value("${sgq.authorizationServer.url}")
	private String autorizationTokenUrl;
	
	@Value("${sgq.authorizationServer.credential}")
	private String credential;
	
	@Value("${sgq.authorizationServer.username}")
	private String username;
	
	@Value("${sgq.authorizationServer.password}")
	private String password;
	
	private String tokenJWT = null;
	
	private Logger logger = org.slf4j.LoggerFactory.getLogger(TransparenciaToken.class);
	
	@PostConstruct
	@Scheduled(fixedDelay = 600000)
	public void initToken()  {
		try {
			logger.info("Atualizando token JWT Interno");
			recuperaToken();
		} catch (RestClientException | URISyntaxException e) {
			logger.error("Erro recuperando token: {}", e.getMessage());
		}
	}
	
	public String getJwtToken() {
		return tokenJWT;
	}
	
	private void recuperaToken() throws RestClientException, URISyntaxException {
		final RestTemplate rt = new RestTemplate();
		
		@SuppressWarnings("rawtypes")
		ResponseEntity<Map> response = rt.exchange(buildEntity(), Map.class);
		this.tokenJWT = (String) response.getBody().get(ACCESS_TOKEN);
		
	}

	private RequestEntity<String> buildEntity() throws URISyntaxException {
		return RequestEntity
				.post(new URI(autorizationTokenUrl))
				.header("Authorization", "Basic " + credential)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.accept(MediaType.APPLICATION_JSON)
				.body(String.format("grant_type=password&username=%s&password=%s&scope=any", username, password));
	}
	
}

