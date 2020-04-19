package net.sgq.incidentes.conformidades.controller;

public interface NCEstadoController {

	void nCMudarParaAberta(Long ncId);

	void nCMudarParaEmAnalise(Long ncId);

	void nCMudarParaConcluida(Long ncId);

}