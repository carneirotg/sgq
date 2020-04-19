package net.sgq.incidentes.artefatos.controller;

import java.net.URISyntaxException;
import java.util.List;

import org.springframework.http.ResponseEntity;

import net.sgq.incidentes.artefatos.modelos.to.ArtefatoIdTO;
import net.sgq.incidentes.artefatos.modelos.to.ArtefatoTO;

public interface ArtefatoController {

	ResponseEntity<List<ArtefatoIdTO>> todos(Integer pagina, Integer registros); 
	
	ResponseEntity<ArtefatoIdTO> buscaArtefatoPor(Long id);
	ResponseEntity<List<ArtefatoIdTO>> buscaArtefatosPor(String nome, Integer pagina, Integer registros);
	
	ResponseEntity<Void> criaArtefato(ArtefatoTO artefatoTO) throws URISyntaxException;
	void atualizaArtefato(Long id, ArtefatoTO artefatoIdTO);
	void depreciaArtefato(Long id);
	
}
