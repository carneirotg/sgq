package net.sgq.transparencia.recall.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import net.sgq.transparencia.recall.modelos.enums.Estado;
import net.sgq.transparencia.recall.modelos.to.CampanhaRecallTO;
import net.sgq.transparencia.recall.servicos.CampanhaService;
import net.sgq.transparencia.utils.handler.PageHeaders;

@RestController
@RequestMapping("/campanhas")
public class CampanhaRecallControllerImpl implements CampanhaRecallController {

	private static final String ROLE_GESTOR = "ROLE_GESTOR";

	@Autowired
	private CampanhaService service;

	@Override
	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CampanhaRecallTO> consultaId(@PathVariable Long id) {

		CampanhaRecallTO cTO = service.consultaId(id);

		if (cTO == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(cTO, HttpStatus.OK);
	}

	@Override
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<CampanhaRecallTO>> todas(
			@RequestParam(defaultValue = "1", required = false) Integer pagina,
			@RequestParam(defaultValue = "10", required = false) Integer registros,
			@RequestParam(defaultValue = "false", required = false) Boolean descSort) {

		Sort sort = Sort.by(Direction.ASC, "id");

		if (descSort) {
			sort = sort.descending();
		}

		Page<CampanhaRecallTO> campanhas = service.buscar(null, null, PageRequest.of(pagina - 1, registros, sort));

		return new ResponseEntity<>(campanhas.getContent(), PageHeaders.headers(campanhas), HttpStatus.OK);
	}

	@Override
	@GetMapping(params = "estado=ativas", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<CampanhaRecallTO>> todasEmAndamento(
			@RequestParam(required = false) String titulo,
			@RequestParam(defaultValue = "1", required = false) Integer pagina,
			@RequestParam(defaultValue = "10", required = false) Integer registros,
			@RequestParam(defaultValue = "false", required = false) Boolean descSort) {

		Sort sort = Sort.by(Direction.ASC, "id");

		if (descSort) {
			sort = sort.descending();
		}

		return trataRespostasBusca(Estado.ATIVA, titulo, PageRequest.of(pagina - 1, registros, sort));
	}

	@Override
	@GetMapping(params = "estado=concluidas", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<CampanhaRecallTO>> todasConcluidas(
			@RequestParam(required = false) String titulo,
			@RequestParam(defaultValue = "1", required = false) Integer pagina,
			@RequestParam(defaultValue = "10", required = false) Integer registros,
			@RequestParam(defaultValue = "false", required = false) Boolean descSort) {

		Sort sort = Sort.by(Direction.ASC, "id");

		if (descSort) {
			sort = sort.descending();
		}

		return trataRespostasBusca(Estado.CONCLUIDA, titulo, PageRequest.of(pagina - 1, registros, sort));
	}

	@Override
	@Secured({ ROLE_GESTOR })
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> novaCampanha(@Validated @RequestBody CampanhaRecallTO campanha) throws URISyntaxException {

		Long id = this.service.salvar(campanha);

		return ResponseEntity.created(new URI("/campanhas/" + id)).build();
	}

	@Override
	@Secured({ ROLE_GESTOR })
	@PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void atualizaCampanha(@PathVariable Long id, @RequestBody CampanhaRecallTO campanha) throws URISyntaxException {
		this.service.atualizaCampanha(id, campanha);
	}
	
	@Override
	@Secured({ ROLE_GESTOR })
	@PatchMapping("/{id}/fim/{novaDataTermino}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void atualizaDataTermino(@PathVariable Long id,
			@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date novaDataTermino) {

		if (novaDataTermino.before(new Date())) {
			throw new IllegalStateException("Nova data de término deve futura.");
		}

		this.service.atualizaDataTermino(id, novaDataTermino);
	}

	@Override
	@Secured({ ROLE_GESTOR })
	@PatchMapping("/{id}/estado/concluida")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void concluiCampanha(@PathVariable Long id) {
		this.service.concluiCampanha(id);
	}

	private ResponseEntity<List<CampanhaRecallTO>> trataRespostasBusca(Estado estado, String titulo, Pageable page) {
		Page<CampanhaRecallTO> campanhasEstado = this.service.buscar(estado, titulo, page);

		if (campanhasEstado.getContent().isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		return new ResponseEntity<>(campanhasEstado.getContent(), PageHeaders.headers(campanhasEstado), HttpStatus.OK);

	}

}
