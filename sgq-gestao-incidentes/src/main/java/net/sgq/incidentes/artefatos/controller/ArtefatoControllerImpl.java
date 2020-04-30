package net.sgq.incidentes.artefatos.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import net.sgq.incidentes.artefatos.modelos.to.ArtefatoIdTO;
import net.sgq.incidentes.artefatos.modelos.to.ArtefatoTO;
import net.sgq.incidentes.artefatos.servicos.ArtefatoService;

@RestController
@RequestMapping("/artefatos")
public class ArtefatoControllerImpl implements ArtefatoController {

	@Autowired
	private ArtefatoService service;

	@Override
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ArtefatoIdTO>> todos(@RequestParam(defaultValue = "1", required = false) Integer pagina,
			@RequestParam(defaultValue = "10", required = false) Integer registros) {

		List<ArtefatoIdTO> artefatos = this.service.buscaArtefatos(null, pagina, registros);

		return new ResponseEntity<>(artefatos, HttpStatus.OK);
	}

	@Override
	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ArtefatoIdTO> buscaArtefatoPor(@PathVariable Long id) {
		
		ArtefatoIdTO artefatoIdTO = this.service.buscaArtefatoPor(id);
		
		if(artefatoIdTO == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(artefatoIdTO, HttpStatus.OK);
	}

	@Override
	@GetMapping(params = { "nome" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ArtefatoIdTO>> buscaArtefatosPor(@RequestParam String nome,
			@RequestParam(defaultValue = "1", required = false) Integer pagina,
			@RequestParam(defaultValue = "10", required = false) Integer registros) {
		List<ArtefatoIdTO> artefatos = this.service.buscaArtefatos(nome, pagina, registros);

		if(artefatos.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(artefatos, HttpStatus.OK);
	}

	@Override
	@PostMapping
	public ResponseEntity<Void> criaArtefato(@Validated @RequestBody ArtefatoTO artefatoTO) throws URISyntaxException {
		Long id = this.service.salvaArtefato(artefatoTO, 0L);
		
		return ResponseEntity.created(new URI("/artefatos/" + id)).build();
		
	}

	@Override
	@PutMapping(path = "/{id}")
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void atualizaArtefato(@PathVariable Long id, @Validated @RequestBody ArtefatoTO artefatoTO) {
		this.service.salvaArtefato(artefatoTO, id);
	}

	@Override
	@PatchMapping("/{id}/depreciado")
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void depreciaArtefato(@PathVariable Long id) {
		this.service.depreciaArtefato(id);
	}

}
