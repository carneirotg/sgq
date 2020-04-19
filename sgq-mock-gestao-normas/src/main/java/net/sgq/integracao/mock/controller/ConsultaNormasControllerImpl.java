package net.sgq.integracao.mock.controller;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.sgq.integracao.mock.model.Norma;
import net.sgq.integracao.mock.model.NormaRepository;

@RestController
@RequestMapping("/normas/")
public class ConsultaNormasControllerImpl implements ConsultaNormasController {

	@Autowired
	private NormaRepository repo;
	
	@Override
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Norma>> listaNormas() {
		return new ResponseEntity<>(repo.findAll(), OK);
	}

	@Override
	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Norma> consultaNorma(@PathVariable Long id) {
		
		Optional<Norma> oNorma = repo.findById(id);
		
		if(oNorma.isEmpty()) {
			return new ResponseEntity<>(NOT_FOUND);
		}
		
		return new ResponseEntity<>(oNorma.get(), OK);
	}

}
