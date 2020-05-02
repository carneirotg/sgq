package net.sgq.relatorios.utils;

import org.springframework.stereotype.Component;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Component
public class RestAuthorizationInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate template) {
		template.header("Authorization", SGQAuthorizationContext.getInstance().getBearerToken());
	}

}
