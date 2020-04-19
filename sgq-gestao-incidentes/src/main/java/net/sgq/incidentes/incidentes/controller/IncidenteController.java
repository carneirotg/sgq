package net.sgq.incidentes.incidentes.controller;

import java.net.URISyntaxException;
import java.util.List;

import org.springframework.http.ResponseEntity;

import net.sgq.incidentes.incidentes.modelos.to.IncidenteIdTO;
import net.sgq.incidentes.incidentes.modelos.to.IncidenteTO;

public interface IncidenteController {

	ResponseEntity<IncidenteIdTO> buscaPor(Long id);
	ResponseEntity<List<IncidenteIdTO>> buscaPor(String descricao);
	ResponseEntity<List<IncidenteIdTO>> listaIncidentes();
	
	ResponseEntity<List<IncidenteIdTO>> incidentesAbertos();
	ResponseEntity<List<IncidenteIdTO>> incidentesEmAnalise();
	ResponseEntity<List<IncidenteIdTO>> incidentesConcluidos();
	
	ResponseEntity<Void> novoIncidente(IncidenteTO incidente) throws URISyntaxException;
	void atualizaIncidente(Long id, IncidenteTO incidente);
	
}
