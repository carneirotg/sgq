package net.sgq.incidentes.artefatos.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import net.sgq.incidentes.artefatos.modelos.Artefato;
import net.sgq.incidentes.artefatos.servicos.ArtefatoService;
import net.sgq.incidentes.utils.handler.PageHeaders;

@RestController
@RequestMapping("/artefatos")
public class ArtefatoControllerImpl implements ArtefatoController {

	@Autowired
	private ArtefatoService service;

	@Override
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Artefato>> todos(@RequestParam(defaultValue = "1", required = false) Integer pagina,
			@RequestParam(defaultValue = "10", required = false) Integer registros) {

		Page<Artefato> artefatos = this.service.buscaArtefatos(null, pagina, registros);
		
		if(pagina > artefatos.getTotalPages()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(artefatos.getContent(), PageHeaders.headers(artefatos), HttpStatus.OK);
	}

	@Override
	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Artefato> buscaArtefatoPor(@PathVariable Long id) {
		
		Artefato artefatoIdTO = this.service.buscaArtefatoPor(id);
		
		if(artefatoIdTO == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(artefatoIdTO, HttpStatus.OK);
	}

	@Override
	@GetMapping(params = { "nome" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Artefato>> buscaArtefatosPor(@RequestParam String nome,
			@RequestParam(defaultValue = "1", required = false) Integer pagina,
			@RequestParam(defaultValue = "10", required = false) Integer registros) {
		Page<Artefato> artefatos = this.service.buscaArtefatos(nome, pagina, registros);

		if(artefatos.getContent().isEmpty() || pagina > artefatos.getTotalPages()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(artefatos.getContent(), PageHeaders.headers(artefatos), HttpStatus.OK);
	}

	@Override
	@PostMapping
	public ResponseEntity<Void> criaArtefato(@Validated @RequestBody Artefato artefato) throws URISyntaxException {
		Long id = this.service.salvaArtefato(artefato, 0L);
		
		return ResponseEntity.created(new URI("/artefatos/" + id)).build();
		
	}

	@Override
	@PutMapping(path = "/{id}")
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void atualizaArtefato(@PathVariable Long id, @Validated @RequestBody Artefato artefato) {
		this.service.salvaArtefato(artefato, id);
	}

	@Override
	@PatchMapping("/{id}/depreciado")
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void depreciaArtefato(@PathVariable Long id) {
		this.service.depreciaArtefato(id);
	}

}
