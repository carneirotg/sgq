package net.sgq.incidentes.conformidades.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import net.sgq.incidentes.conformidades.modelos.Norma;

public interface NCNormaController {

	void associarNorma(Long ncId, Long normaId);

	ResponseEntity<Norma> buscaNormaNC(Long ncId);

	void ncAtualizaChecklist(Long ncId, Map<String, Boolean> checklist);

}