package net.sgq.transparencia.utils.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import net.sgq.transparencia.config.TransparenciaToken;

@Component
public class InternalAuthorizationRequestInterceptor implements RequestInterceptor {

	@Autowired
	private TransparenciaToken token;
	
	@Override
	public void apply(RequestTemplate template) {
		template.header("Authorization", "Bearer " + token.getJwtToken());
	}

}
