package net.sgq.gateway.normas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.sgq.gateway.normas.modelos.Norma;
import net.sgq.gateway.normas.servicos.NormaService;

@RestController
@RequestMapping("/normas")
public class NormaControllerImpl implements NormaController {

	@Autowired
	private NormaService service;

	@Override
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Norma>> listaNormas() {
		List<Norma> normas = this.service.listaNormas();
		
		if (normas == null) {
			return new ResponseEntity<>(normas, HttpStatus.BAD_GATEWAY);
		}
		
		if(normas.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(normas, HttpStatus.OK);
	}

	@Override
	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Norma> consultaNorma(@PathVariable Long id) {
		Norma consultaNorma = this.service.consultaNorma(id);
		
		if(consultaNorma == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(consultaNorma, HttpStatus.OK);
	}

}
