package net.sgq.incidentes.incidentes.controller;

public interface IncidenteNCController {

	void adicionaNaoConformidade(Long iId, Long ncId);
	void removeNaoConformidade(Long iId, Long ncId);
	void removeTodasNCs(Long iId);
	
}
