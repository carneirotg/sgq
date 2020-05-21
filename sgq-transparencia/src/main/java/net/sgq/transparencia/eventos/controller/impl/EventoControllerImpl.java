package net.sgq.transparencia.eventos.controller.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.sgq.transparencia.clientes.GestaoIncidentesClient;
import net.sgq.transparencia.clientes.to.IncidenteTO;
import net.sgq.transparencia.clientes.to.Incidentes;
import net.sgq.transparencia.eventos.controller.EventoController;
import net.sgq.transparencia.recall.modelos.Campanhas;
import net.sgq.transparencia.recall.modelos.enums.Estado;
import net.sgq.transparencia.recall.modelos.to.CampanhaRecallTO;
import net.sgq.transparencia.recall.servicos.CampanhaService;
import net.sgq.transparencia.utils.handler.PageHeaders;

@RestController
@RequestMapping("/eventos")
public class EventoControllerImpl implements EventoController {

	@Autowired
	private GestaoIncidentesClient client;

	@Autowired
	private CampanhaService campanhaService;

	@GetMapping(path = "/incidentes", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<IncidenteTO>> listaJsonIncidentes(
			@RequestParam(defaultValue = "1", required = false) Integer pagina,
			@RequestParam(defaultValue = "10", required = false) Integer registros) {
		return this.client.consultaIncidentesConcluidos(pagina, registros);
	}

	@GetMapping(path = "/incidentes", produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<Incidentes> listaXmlIncidentes(
			@RequestParam(defaultValue = "1", required = false) Integer pagina,
			@RequestParam(defaultValue = "10", required = false) Integer registros) {

		ResponseEntity<List<IncidenteTO>> responseGestaoIncidentes = this.client.consultaIncidentesConcluidos(pagina,
				registros);
		List<IncidenteTO> incidendentesConcluidos = responseGestaoIncidentes.getBody();

		return ResponseEntity.ok().headers(c -> c.addAll(trataHeadersPaginacao(responseGestaoIncidentes.getHeaders())))
				.contentType(MediaType.APPLICATION_XML).body(new Incidentes(incidendentesConcluidos));
	}

	@GetMapping(path = "/campanhas", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<CampanhaRecallTO>> listaJsonCampanhas(
			@RequestParam(defaultValue = "1", required = false) Integer pagina,
			@RequestParam(defaultValue = "10", required = false) Integer registros) {

		PageRequest pageRequest = PageRequest.of(pagina - 1, registros, Sort.by(Direction.DESC, "id"));
		Page<CampanhaRecallTO> campanhas = campanhaService.buscar(Estado.ATIVA, null, pageRequest);
		
		if(campanhas.getContent().isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(campanhas.getContent(), PageHeaders.headers(campanhas), HttpStatus.OK);
	}

	@GetMapping(path = "/campanhas", produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<Campanhas> listaXmlCampanhas(
			@RequestParam(defaultValue = "1", required = false) Integer pagina,
			@RequestParam(defaultValue = "10", required = false) Integer registros) {

		PageRequest pageRequest = PageRequest.of(pagina - 1, registros, Sort.by(Direction.DESC, "id"));
		Page<CampanhaRecallTO> campanhas = campanhaService.buscar(Estado.ATIVA, null, pageRequest);

		if(campanhas.getContent().isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		MultiValueMap<String, String> headers = PageHeaders.headers(campanhas);
		return new ResponseEntity<>(new Campanhas(campanhas.getContent()), headers, HttpStatus.OK);
	}

	private HttpHeaders trataHeadersPaginacao(HttpHeaders httpHeaders) {
		final HttpHeaders headers = new HttpHeaders();

		httpHeaders.entrySet().stream().filter(e -> e.getKey().contains("x-sgq-"))
				.forEach(e -> headers.add(e.getKey(), e.getValue().get(0)));

		return headers;
	}

}
