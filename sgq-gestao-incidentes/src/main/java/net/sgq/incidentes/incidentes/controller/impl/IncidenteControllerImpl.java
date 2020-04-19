package net.sgq.incidentes.incidentes.controller.impl;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import net.sgq.incidentes.conformidades.modelos.enums.Estado;
import net.sgq.incidentes.incidentes.controller.IncidenteController;
import net.sgq.incidentes.incidentes.modelos.to.IncidenteIdTO;
import net.sgq.incidentes.incidentes.modelos.to.IncidenteTO;
import net.sgq.incidentes.incidentes.servicos.IncidenteService;

@RestController
@RequestMapping("/incidentes")
public class IncidenteControllerImpl implements IncidenteController {

	@Autowired
	private IncidenteService service;
	
	@Override
	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<IncidenteIdTO> buscaPor(@PathVariable Long id) {
		IncidenteIdTO ic = this.service.consultaIncidente(id);
		
		if(ic == null) {
			return new ResponseEntity<>(NOT_FOUND);
		}
		
		return new ResponseEntity<>(ic, OK);
	}

	@Override
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, params = {"!nome", "!estado"})
	public ResponseEntity<List<IncidenteIdTO>> listaIncidentes() {
		List<IncidenteIdTO> incidentes = this.service.listaIncidentes();
		return new ResponseEntity<>(incidentes, HttpStatus.OK);
	}
	
	@Override
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, params = {"nome"})
	public ResponseEntity<List<IncidenteIdTO>> buscaPor(@RequestParam String nome) {
		List<IncidenteIdTO> incidentes = this.service.listaIncidentes(nome);
		
		if(incidentes.isEmpty()) {
			return new ResponseEntity<>(NOT_FOUND);
		}
		
		return new ResponseEntity<>(incidentes, OK);
	}

	@Override
	@GetMapping(params = "estado=abertos")
	public ResponseEntity<List<IncidenteIdTO>> incidentesAbertos() {
		return listaPorEstado(Estado.ABERTA);
	}

	@Override
	@GetMapping(params = "estado=em_analise")
	public ResponseEntity<List<IncidenteIdTO>> incidentesEmAnalise() {
		return listaPorEstado(Estado.EM_ANALISE);
	}

	@Override
	@GetMapping(params = "estado=concluidos")
	public ResponseEntity<List<IncidenteIdTO>> incidentesConcluidos() {
		return listaPorEstado(Estado.CONCLUIDA);
	}

	@Override
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> novoIncidente(@RequestBody IncidenteTO incidente) throws URISyntaxException {
		Long id = this.service.salvarIncidente(incidente, 0L);
		return ResponseEntity.created(new URI("/incidentes/" + id)).build();
	}

	@Override
	@PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(code = NO_CONTENT)
	public void atualizaIncidente(@PathVariable Long id, @RequestBody IncidenteTO incidente) {
		this.service.salvarIncidente(incidente, id);
	}
	
	private ResponseEntity<List<IncidenteIdTO>> listaPorEstado(Estado estado) {
		List<IncidenteIdTO> incidentes = this.service.listaIncidentes(estado);
		
		if(incidentes.isEmpty()) {
			return new ResponseEntity<>(NOT_FOUND);
		}
		
		return new ResponseEntity<>(incidentes, OK);
	}

}
