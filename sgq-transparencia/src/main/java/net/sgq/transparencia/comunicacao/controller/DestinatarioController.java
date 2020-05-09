package net.sgq.transparencia.comunicacao.controller;

import java.net.URISyntaxException;
import java.util.List;

import org.springframework.http.ResponseEntity;

import net.sgq.transparencia.comunicacao.modelos.to.DestinatarioTO;

public interface DestinatarioController {

	ResponseEntity<List<DestinatarioTO>> todos();

	ResponseEntity<Void> novoDestinatario(DestinatarioTO to) throws URISyntaxException;
	
	void atualizaDestinatario(DestinatarioTO to, Long id) throws URISyntaxException;

	void removerDestinatario(Long id);

}