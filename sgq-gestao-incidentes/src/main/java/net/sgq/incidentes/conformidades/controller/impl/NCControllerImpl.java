package net.sgq.incidentes.conformidades.controller.impl;

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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import net.sgq.incidentes.conformidades.controller.NCController;
import net.sgq.incidentes.conformidades.modelos.NaoConformidade;
import net.sgq.incidentes.conformidades.modelos.enums.Estado;
import net.sgq.incidentes.conformidades.servicos.NaoConformidadeService;
import net.sgq.incidentes.utils.handler.PageHeaders;

@RestController
@RequestMapping("/ncs")
public class NCControllerImpl implements NCController {

	@Autowired
	private NaoConformidadeService service;

	@Override
	@GetMapping(params = { "!titulo", "!estado" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<NaoConformidade>> listaNCs(
			@RequestParam(defaultValue = "1", required = false) Integer pagina,
			@RequestParam(defaultValue = "10", required = false) Integer registros) {
		Page<NaoConformidade> ncs = this.service.listaNCs(PageRequest.of(pagina - 1, registros));
		return new ResponseEntity<>(ncs.getContent(), PageHeaders.headers(ncs), OK);
	}

	@Override
	@GetMapping(params = { "titulo" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<NaoConformidade>> consultaNC(@RequestParam String titulo,
			@RequestParam(defaultValue = "1", required = false) Integer pagina,
			@RequestParam(defaultValue = "10", required = false) Integer registros) {
		Page<NaoConformidade> nc = this.service.listaNCs(titulo, PageRequest.of(pagina - 1, registros));

		if (nc.getContent().isEmpty()) {
			return new ResponseEntity<>(NOT_FOUND);
		}

		return new ResponseEntity<>(nc.getContent(), PageHeaders.headers(nc), OK);
	}

	@Override
	@GetMapping(path = "/{id:^[0-9]+}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<NaoConformidade> consultaNC(@PathVariable Long id) {
		NaoConformidade nc = this.service.consultaNC(id);

		if (nc == null) {
			return new ResponseEntity<>(NOT_FOUND);
		}

		return new ResponseEntity<>(nc, OK);
	}

	@Override
	@GetMapping(params = "estado=abertas")
	public ResponseEntity<List<NaoConformidade>> nCsAbertas(
			@RequestParam(required = false) String titulo,
			@RequestParam(defaultValue = "1", required = false) Integer pagina,
			@RequestParam(defaultValue = "10", required = false) Integer registros) {
		return listaPorEstado(Estado.ABERTA, titulo, PageRequest.of(pagina - 1, registros));
	}

	@Override
	@GetMapping(params = "estado=em_analise")
	public ResponseEntity<List<NaoConformidade>> nCsEmAnalise(
			@RequestParam(required = false) String titulo,
			@RequestParam(defaultValue = "1", required = false) Integer pagina,
			@RequestParam(defaultValue = "10", required = false) Integer registros) {
		return listaPorEstado(Estado.EM_ANALISE, titulo, PageRequest.of(pagina - 1, registros));
	}

	@Override
	@GetMapping(params = "estado=concluidas")
	public ResponseEntity<List<NaoConformidade>> nCsConcluidas(
			@RequestParam(required = false) String titulo,
			@RequestParam(defaultValue = "1", required = false) Integer pagina,
			@RequestParam(defaultValue = "10", required = false) Integer registros) {
		return listaPorEstado(Estado.CONCLUIDA, titulo, PageRequest.of(pagina - 1, registros));
	}

	@Override
	@GetMapping(params = "estado=nao_concluidas")
	public ResponseEntity<List<NaoConformidade>> nCsNaoConcluidas(
			@RequestParam(required = false) String titulo,
			@RequestParam(defaultValue = "1", required = false) Integer pagina,
			@RequestParam(defaultValue = "10", required = false) Integer registros) {
		return listaPorEstado(Estado.NAO_CONCLUIDA, titulo, PageRequest.of(pagina - 1, registros));
	}

	@Override
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> novaNC(@RequestBody NaoConformidade naoConformidadeTo) throws URISyntaxException {
		Long id = this.service.salvarNC(naoConformidadeTo, 0L);

		return ResponseEntity.created(new URI("/ncs/" + id)).build();
	}

	@Override
	@PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(code = NO_CONTENT)
	public void atualizaNC(@PathVariable Long id, @RequestBody NaoConformidade naoConformidadeTo) {
		this.service.salvarNC(naoConformidadeTo, id);
	}

	private ResponseEntity<List<NaoConformidade>> listaPorEstado(Estado estado, String titulo, Pageable page) {
		Page<NaoConformidade> ncs = this.service.listaNCs(estado, titulo, page);

		if (ncs.getContent().isEmpty()) {
			return new ResponseEntity<>(NOT_FOUND);
		}

		return new ResponseEntity<>(ncs.getContent(), PageHeaders.headers(ncs), OK);
	}

}
