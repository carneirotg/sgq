package net.sgq.transparencia.comunicacao.controller;

import java.net.URISyntaxException;
import java.util.List;

import org.springframework.http.ResponseEntity;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.sgq.transparencia.comunicacao.modelos.to.DestinatarioTO;

public interface DestinatarioController {

	@ApiOperation(value = "Lista todos os destinatários", response = DestinatarioTO.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Lista com destinatários") })
	ResponseEntity<List<DestinatarioTO>> todos();

	@ApiOperation(value = "Cadastra um novo destinatário", response = Void.class)
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Destinatário cadastrado com sucesso") })
	ResponseEntity<Void> novoDestinatario(DestinatarioTO to) throws URISyntaxException;

	@ApiOperation(value = "Atualiza um destinatário", response = Void.class)
	@ApiResponses(value = { @ApiResponse(code = 204, message = "Destinatário atualizado com sucesso") })
	void atualizaDestinatario(DestinatarioTO to, Long id) throws URISyntaxException;

	@ApiOperation(value = "Remove um destinatário", response = Void.class)
	@ApiResponses(value = { @ApiResponse(code = 204, message = "Destinatário removido com sucesso") })
	void removerDestinatario(Long id);

}