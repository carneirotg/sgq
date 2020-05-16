package net.sgq.transparencia.utils.handler;

import org.springframework.data.domain.Page;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class PageHeaders {

	public static MultiValueMap<String, String> headers(Page<?> p) {
		MultiValueMap<String, String> h = new LinkedMultiValueMap<>();
		h.add("x-sgq-pagina", String.valueOf(p.getNumber() + 1));
		h.add("x-sgq-paginas", String.valueOf(p.getTotalPages()));
		h.add("x-sgq-total", String.valueOf(p.getTotalElements()));
		
		return h;
	}
	
}
