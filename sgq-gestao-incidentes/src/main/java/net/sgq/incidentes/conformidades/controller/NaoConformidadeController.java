package net.sgq.incidentes.conformidades.controller;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import net.sgq.incidentes.conformidades.modelos.Norma;
import net.sgq.incidentes.conformidades.modelos.to.NaoConformidadeIdTO;
import net.sgq.incidentes.conformidades.modelos.to.NaoConformidadeTO;

public interface NaoConformidadeController {

	ResponseEntity<List<NaoConformidadeIdTO>> listaNCs();
	ResponseEntity<List<NaoConformidadeIdTO>> consultaNC(String nome);
	ResponseEntity<NaoConformidadeIdTO> consultaNC(Long id);
	ResponseEntity<Norma> buscaNormaNC(Long ncId);
	
	
	ResponseEntity<Void> novaNC(NaoConformidadeTO naoConformidadeTo) throws URISyntaxException;
	void atualizaNC(Long id, NaoConformidadeTO naoConformidadeTo);

	ResponseEntity<List<NaoConformidadeIdTO>> nCsAbertas();
	ResponseEntity<List<NaoConformidadeIdTO>> nCsEmAnalise();
	ResponseEntity<List<NaoConformidadeIdTO>> nCsConcluidas();
	
	void ncAtualizaChecklist(Long id, Map<String, Boolean> ncAtualizaChecklist);
	
	void associarNorma(Long ncId, Long normaId);
	
	void nCMudarParaAberta(Long id);
	void nCMudarParaEmAnalise(Long id);
	void nCMudarParaConcluida(Long id);
	
	
}
