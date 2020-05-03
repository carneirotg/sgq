package net.sgq.gateway.normas.cliente;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import net.sgq.gateway.normas.modelos.Norma;

@Component
public class GestaoNormaClient {

	@Value("${sgq.integracao.gestao-normas.host}")
	private String urlGN;

	@Autowired
	private RestTemplate restTemplate;

	public List<Norma> consultaListaNormas() {
		ResponseEntity<List<Map<String, Object>>> responseEntity = restTemplate.exchange(this.urlGN + "/normas/",
				HttpMethod.GET, null, new ParameterizedTypeReference<List<Map<String, Object>>>() {
				});

		if (responseEntity == null || responseEntity.getStatusCode().is5xxServerError()) {
			throw new IllegalStateException("Client retornou vazio / erro para lista de normas");
		}

		return responseEntity.getBody().stream().map(Norma::new).collect(Collectors.toList());
	}

	public Norma consultaNorma(Long id) {
		@SuppressWarnings("unchecked")
		Map<String, Object> responseEntity = restTemplate.getForObject(this.urlGN + "/normas/{id}", Map.class,
				Long.toString(id));

		if (responseEntity == null) {
			throw new IllegalStateException("Client retornou vazio para norma");
		}

		return new Norma(responseEntity);
	}

}
