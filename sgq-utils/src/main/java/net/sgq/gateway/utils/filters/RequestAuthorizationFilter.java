package net.sgq.gateway.utils.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import net.sgq.gateway.utils.contexts.SGQAuthorizationContext;

@Component
public class RequestAuthorizationFilter implements Filter {

	private static final String AUTHORIZATION = "Authorization";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpReq = (HttpServletRequest) request;
		String authHeader = httpReq.getHeader(AUTHORIZATION);
		
		if(authHeader != null && !"".equalsIgnoreCase(authHeader)) {
			SGQAuthorizationContext.getInstance().setBearerToken(authHeader);
		}
		
		chain.doFilter(request, response);
		
	}

}
