package net.sgq.transparencia.clientes;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

import feign.Headers;
import net.sgq.transparencia.clientes.to.AccessTokenTO;
import net.sgq.transparencia.config.FeignConfig;

@FeignClient(name = "authorization-server", configuration = FeignConfig.class)
public interface AuthServerClient {

	@PostMapping(path="/oauth/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@Headers(value = "Content-Type: application/x-www-form-urlencoded")
	public AccessTokenTO login(Map<String, ?> formParams);
	
}
