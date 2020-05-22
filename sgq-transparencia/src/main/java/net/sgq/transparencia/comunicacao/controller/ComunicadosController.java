package net.sgq.transparencia.comunicacao.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import net.sgq.transparencia.comunicacao.modelos.Envio;

public interface ComunicadosController {

	ResponseEntity<List<Envio>> todos(Integer pagina, Integer registros);
	
}
