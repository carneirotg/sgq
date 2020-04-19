package net.sgq.incidentes.incidentes.controller;

public interface IncidenteEstadoController {

	void incidenteMudarParaAberta(Long iId);

	void incidenteMudarParaEmAnalise(Long iId);

	void incidenteMudarParaConcluida(Long iId);
	
}
