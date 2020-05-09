package net.sgq.transparencia.comunicacao.controller.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import net.sgq.transparencia.comunicacao.controller.DestinatarioController;
import net.sgq.transparencia.comunicacao.modelos.to.DestinatarioTO;
import net.sgq.transparencia.comunicacao.servicos.DestinatarioService;

@RestController
@RequestMapping("/destinatarios")
@CrossOrigin(exposedHeaders = {"Location"})
public class DestinatarioControllerImpl implements DestinatarioController {

	@Autowired
	private DestinatarioService service;

	@Override
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<DestinatarioTO>> todos() {
		return new ResponseEntity<>(this.service.todos(), HttpStatus.OK);
	}

	@Override
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> novoDestinatario(@RequestBody DestinatarioTO to) throws URISyntaxException {
		Long destinatarioId = this.service.salvarDestinatario(to, 0L);

		return ResponseEntity.created(new URI("/destinatarios/" + destinatarioId)).build();
	}

	@Override
	@PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void atualizaDestinatario(@RequestBody DestinatarioTO to, @PathVariable Long id) throws URISyntaxException {
		this.service.salvarDestinatario(to, id);
	}

	@Override
	@DeleteMapping("/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removerDestinatario(@PathVariable Long id) {
		this.service.removeDestinatario(id);
	}

}