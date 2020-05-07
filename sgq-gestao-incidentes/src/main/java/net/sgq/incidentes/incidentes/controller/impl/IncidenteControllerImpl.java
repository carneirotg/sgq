package net.sgq.incidentes.incidentes.controller.impl;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import net.sgq.incidentes.conformidades.modelos.enums.Estado;
import net.sgq.incidentes.incidentes.controller.IncidenteController;
import net.sgq.incidentes.incidentes.modelos.Incidente;
import net.sgq.incidentes.incidentes.servicos.IncidenteService;
import net.sgq.incidentes.utils.handler.PageHeaders;

@RestController
@RequestMapping("/incidentes")
public class IncidenteControllerImpl implements IncidenteController {

	@Autowired
	private IncidenteService service;

	@Override
	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Incidente> buscaPor(@PathVariable Long id) {
		Incidente ic = this.service.consultaIncidente(id);

		if (ic == null) {
			return new ResponseEntity<>(NOT_FOUND);
		}

		return new ResponseEntity<>(ic, OK);
	}

	@Override
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, params = { "!nome", "!estado" })
	public ResponseEntity<List<Incidente>> listaIncidentes(
			@RequestParam(defaultValue = "1", required = false) Integer pagina,
			@RequestParam(defaultValue = "10", required = false) Integer registros) {
		Page<Incidente> incidentes = this.service.listaIncidentes(PageRequest.of(pagina, registros));
		return new ResponseEntity<>(incidentes.getContent(), PageHeaders.headers(incidentes), HttpStatus.OK);
	}

	@Override
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, params = { "nome" })
	public ResponseEntity<List<Incidente>> buscaPor(@RequestParam String nome,
			@RequestParam(defaultValue = "1", required = false) Integer pagina,
			@RequestParam(defaultValue = "10", required = false) Integer registros) {
		Page<Incidente> incidentes = this.service.listaIncidentes(nome, PageRequest.of(pagina, registros));

		if (incidentes.getContent().isEmpty()) {
			return new ResponseEntity<>(NOT_FOUND);
		}

		return new ResponseEntity<>(incidentes.getContent(), PageHeaders.headers(incidentes), OK);
	}

	@Override
	@GetMapping(params = "estado=abertos")
	public ResponseEntity<List<Incidente>> incidentesAbertos(
			@RequestParam(defaultValue = "1", required = false) Integer pagina,
			@RequestParam(defaultValue = "10", required = false) Integer registros) {
		return listaPorEstado(Estado.ABERTA, null, PageRequest.of(pagina, registros));
	}

	@Override
	@GetMapping(params = "estado=em_analise")
	public ResponseEntity<List<Incidente>> incidentesEmAnalise(
			@RequestParam(defaultValue = "1", required = false) Integer pagina,
			@RequestParam(defaultValue = "10", required = false) Integer registros) {
		return listaPorEstado(Estado.EM_ANALISE, null, PageRequest.of(pagina, registros));
	}

	@Override
	@GetMapping(params = "estado=concluidos")
	public ResponseEntity<List<Incidente>> incidentesConcluidos(
			@RequestParam(defaultValue = "1", required = false) Integer pagina,
			@RequestParam(defaultValue = "10", required = false) Integer registros) {
		return listaPorEstado(Estado.CONCLUIDA, null, PageRequest.of(pagina, registros));
	}

	@Override
	@GetMapping(params = { "estado=concluidos", "janelaMinutos" })
	public ResponseEntity<List<Incidente>> incidentesConcluidosJanela(
			@RequestParam(defaultValue = "30") Integer janelaMinutos,
			@RequestParam(defaultValue = "1", required = false) Integer pagina,
			@RequestParam(defaultValue = "10", required = false) Integer registros) {
		return listaPorEstado(Estado.CONCLUIDA, janelaMinutos, PageRequest.of(pagina, registros));
	}

	@Override
	@GetMapping(params = "estado=nao_concluidos")
	public ResponseEntity<List<Incidente>> incidentesNaoConcluidos(
			@RequestParam(defaultValue = "1", required = false) Integer pagina,
			@RequestParam(defaultValue = "10", required = false) Integer registros) {
		return listaPorEstado(Estado.NAO_CONCLUIDA, null, PageRequest.of(pagina, registros));
	}

	@Override
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> novoIncidente(@Validated @RequestBody Incidente incidente) throws URISyntaxException {
		Long id = this.service.salvarIncidente(incidente, 0L);
		return ResponseEntity.created(new URI("/incidentes/" + id)).build();
	}

	@Override
	@PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(code = NO_CONTENT)
	public void atualizaIncidente(@PathVariable Long id, @Validated @RequestBody Incidente incidente) {
		this.service.salvarIncidente(incidente, id);
	}

	private ResponseEntity<List<Incidente>> listaPorEstado(Estado estado, Integer janelaMinutos, Pageable pageable) {
		Page<Incidente> incidentes = this.service.listaIncidentes(estado, janelaMinutos, pageable);

		if (incidentes.getContent().isEmpty()) {
			return new ResponseEntity<>(NOT_FOUND);
		}

		return new ResponseEntity<>(incidentes.getContent(), PageHeaders.headers(incidentes), OK);
	}

}
