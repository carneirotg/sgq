package net.sgq.gateway.router;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

@Component
@ConfigurationProperties("sgq")
public class SGQRouterFilter extends ZuulFilter {

	private Map<String, Map<String, Map<String, String>>> rotas;
	
	private Logger logger = LoggerFactory.getLogger(SGQRouterFilter.class);

	@Override
	public boolean shouldFilter() {

		String serviceId = (String) RequestContext.getCurrentContext().get("serviceId");

		if (serviceId != null) {
			return rotas.containsKey(serviceId);
		}

		return false;
	}

	@Override
	public Object run() throws ZuulException {

		RequestContext context = RequestContext.getCurrentContext();
		context.put("requestURI", trataModuloEVersao(context));
		return null;
	}

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return FilterConstants.PRE_DECORATION_FILTER_ORDER + 1;
	}

	public Map<String, Map<String, Map<String, String>>> getRotas() {
		return rotas;
	}

	public void setRotas(Map<String, Map<String, Map<String, String>>> rotas) {
		this.rotas = rotas;
	}

	private String trataModuloEVersao(RequestContext context) {

		try {
			final String modulo = (String) context.get("serviceId");
			final String proxy = (String) context.get("proxy");
			final String versao = tratarVersao(proxy);
			final String recurso = proxy.replace("-".concat(versao), "");
			final String moduloPathVersao = rotas.get(modulo).get(recurso).get(versao);

			return moduloPathVersao + context.get("requestURI").toString();
		} catch(Exception e) {
			logger.debug("Erro ao rotear para padrão SGQ. Retornando ao padrão para o request: {}", context.entrySet());
			return context.get("requestURI").toString();
		}
	}

	private String tratarVersao(String proxy) {
		String[] versaoSplit = proxy.split("-");

		if (versaoSplit.length == 1) {
			return "v1";
		}

		return versaoSplit[versaoSplit.length - 1];
	}

}
