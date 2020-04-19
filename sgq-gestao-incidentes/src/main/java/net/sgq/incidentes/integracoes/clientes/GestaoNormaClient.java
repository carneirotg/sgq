package net.sgq.incidentes.integracoes.clientes;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import net.sgq.incidentes.conformidades.modelos.Norma;

@Component
public class GestaoNormaClient {

	@Value("${sgq.integracao.gestao-normas.host}")
	private String urlGN;

	@Value("${sgq.integracao.gestao-normas.credencial}")
	private String credencial;

	private RestTemplate restTemplate = new RestTemplate();

	public List<Norma> consultaListaNormas() {
		ResponseEntity<List<Map<String, Object>>> responseEntity = restTemplate.exchange(this.urlGN + "/normas/",
				HttpMethod.GET, null, new ParameterizedTypeReference<List<Map<String, Object>>>() {
				});

		if (responseEntity == null) {
			throw new IllegalStateException("Client retornou vazio para lista de normas");
		}

		return responseEntity.getBody().stream().map(resp -> new Norma(resp)).collect(Collectors.toList());
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
