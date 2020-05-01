package net.sgq.gateway.router;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

@SpringBootTest
public class SGQRouterFilterTests {

	@Autowired
	private SGQRouterFilter router;

	@Test
	public void routerOk() {
		assertThat(router).isNotNull();
	}

	@Test
	public void roteamentoPorServiceIdOuPadrao() {
		assertThat(router.shouldFilter()).isFalse();

		RequestContext.getCurrentContext().set("serviceId", "abc123");
		assertThat(router.shouldFilter()).isFalse();

		RequestContext.getCurrentContext().set("serviceId", "gestao-incidentes");
		assertThat(router.shouldFilter()).isTrue();
	}

	@Test
	public void configuraContextoDeRotasSGQ() throws ZuulException {
		RequestContext context = RequestContext.getCurrentContext();
		context.set("serviceId", "gestao-incidentes");
		context.set("proxy", "incidentes-v1");
		context.set("requestURI", "");
		router.run();

		assertThat(context.get("requestURI")).isEqualTo("/v1/incidentes");
	}
	
	@Test
	public void configuraContextoDeRotasSGQFallbackErro() throws ZuulException {
		RequestContext context = RequestContext.getCurrentContext();
		context.set("serviceId", "gestao-incidentes");
		context.set("proxy", null);
		context.set("requestURI", "");
		router.run();

		assertThat(context.get("requestURI")).isEqualTo("");
	}

}
