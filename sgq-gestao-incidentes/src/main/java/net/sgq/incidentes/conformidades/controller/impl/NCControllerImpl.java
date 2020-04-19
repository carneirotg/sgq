package net.sgq.incidentes.conformidades.controller.impl;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import net.sgq.incidentes.conformidades.modelos.enums.Estado;
import net.sgq.incidentes.conformidades.modelos.to.NaoConformidadeIdTO;
import net.sgq.incidentes.conformidades.modelos.to.NaoConformidadeTO;
import net.sgq.incidentes.conformidades.servicos.NaoConformidadeService;

@RestController
@RequestMapping("/ncs/")
public class NCControllerImpl implements NCController {

	@Autowired
	private NaoConformidadeService service;

	@Override
	@GetMapping(params = {"!nome", "!estado"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<NaoConformidadeIdTO>> listaNCs() {
		List<NaoConformidadeIdTO> ncs = this.service.listaNCs();
		return new ResponseEntity<>(ncs, OK);
	}

	@Override
	@GetMapping(params = {"nome"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<NaoConformidadeIdTO>> consultaNC(@RequestParam String nome) {
		List<NaoConformidadeIdTO> nc = this.service.listaNCs(nome);
		
		if(nc.isEmpty()) {
			return new ResponseEntity<>(NOT_FOUND);
		}
		
		return new ResponseEntity<>(nc, OK);
	}

	@Override
	@GetMapping(path = "/{id:^[0-9]+}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<NaoConformidadeIdTO> consultaNC(@PathVariable Long id) {
		NaoConformidadeIdTO nc = this.service.consultaNC(id);
		
		if(nc == null) {
			return new ResponseEntity<>(NOT_FOUND);
		}
		
		return new ResponseEntity<>(nc, OK);
	}
	
	@Override
	@GetMapping(params = "estado=abertas")
	public ResponseEntity<List<NaoConformidadeIdTO>> nCsAbertas() {
		return listaPorEstado(Estado.ABERTA);
	}

	@Override
	@GetMapping(params = "estado=em_analise")
	public ResponseEntity<List<NaoConformidadeIdTO>> nCsEmAnalise() {
		return listaPorEstado(Estado.EM_ANALISE);
	}

	@Override
	@GetMapping(params = "estado=concluidas")
	public ResponseEntity<List<NaoConformidadeIdTO>> nCsConcluidas() {
		return listaPorEstado(Estado.CONCLUIDA);
	}

	@Override
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> novaNC(@RequestBody NaoConformidadeTO naoConformidadeTo) throws URISyntaxException {
		Long id = this.service.salvarNC(naoConformidadeTo, 0L);
		
		return ResponseEntity.created(new URI("/ncs/" + id)).build();
	}

	@Override
	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(code = NO_CONTENT)
	public void atualizaNC(Long id, NaoConformidadeTO naoConformidadeTo) {
		this.service.salvarNC(naoConformidadeTo, id);		
	}

	private ResponseEntity<List<NaoConformidadeIdTO>> listaPorEstado(Estado estado) {
		List<NaoConformidadeIdTO> ncs = this.service.listaNCs(estado);
		
		if(ncs.isEmpty()) {
			return new ResponseEntity<>(NOT_FOUND);
		}
		
		return new ResponseEntity<>(ncs, OK);
	}

}
