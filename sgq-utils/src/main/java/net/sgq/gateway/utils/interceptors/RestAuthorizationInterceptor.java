package net.sgq.gateway.utils.interceptors;

import org.springframework.stereotype.Component;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import net.sgq.gateway.utils.contexts.SGQAuthorizationContext;

@Component
public class RestAuthorizationInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate template) {
		template.header("Authorization", SGQAuthorizationContext.getInstance().getBearerToken());
	}

}
