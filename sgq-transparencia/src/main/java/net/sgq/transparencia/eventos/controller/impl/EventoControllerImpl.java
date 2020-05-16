package net.sgq.transparencia.eventos.controller.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.sgq.transparencia.clientes.GestaoIncidentesClient;
import net.sgq.transparencia.clientes.to.IncidenteTO;
import net.sgq.transparencia.clientes.to.Incidentes;
import net.sgq.transparencia.eventos.controller.EventoController;

@RestController
@RequestMapping("/eventos")
public class EventoControllerImpl implements EventoController {

	@Autowired
	private GestaoIncidentesClient client;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<IncidenteTO>> listaJson(
			@RequestParam(defaultValue = "1", required = false) Integer pagina,
			@RequestParam(defaultValue = "10", required = false) Integer registros) {
		return this.client.consultaIncidentesConcluidos(pagina, registros);
	}

	@GetMapping(produces = MediaType.APPLICATION_XML_VALUE)	
	public ResponseEntity<Incidentes> listaXml(@RequestParam(defaultValue = "1", required = false) Integer pagina,
			@RequestParam(defaultValue = "10", required = false) Integer registros) {

		ResponseEntity<List<IncidenteTO>> responseGestaoIncidentes = this.client.consultaIncidentesConcluidos(pagina,
				registros);
		List<IncidenteTO> incidendentesConcluidos = responseGestaoIncidentes.getBody();

		return ResponseEntity.ok().headers(c -> c.addAll(trataHeadersPaginacao(responseGestaoIncidentes.getHeaders())))
				.contentType(MediaType.APPLICATION_XML).body(new Incidentes(incidendentesConcluidos));
	}

	private HttpHeaders trataHeadersPaginacao(HttpHeaders httpHeaders) {
		final HttpHeaders headers = new HttpHeaders();

		httpHeaders.entrySet().stream().filter(e -> e.getKey().contains("x-sgq-"))
				.forEach(e -> headers.add(e.getKey(), e.getValue().get(0)));
		
		return headers;
	}

}
