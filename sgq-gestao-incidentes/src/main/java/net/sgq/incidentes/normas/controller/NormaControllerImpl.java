package net.sgq.incidentes.normas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.sgq.incidentes.conformidades.modelos.Norma;
import net.sgq.incidentes.normas.servicos.NormaService;

@RestController
@RequestMapping("/normas")
public class NormaControllerImpl implements NormaController {

	@Autowired
	private NormaService service;

	@Override
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Norma>> listaNormas() {
		return new ResponseEntity<>(this.service.listaNormas(), HttpStatus.OK);
	}

	@Override
	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Norma> consultaNorma(@PathVariable Long id) {
		return new ResponseEntity<>(this.service.consultaNorma(id), HttpStatus.OK);
	}

}
