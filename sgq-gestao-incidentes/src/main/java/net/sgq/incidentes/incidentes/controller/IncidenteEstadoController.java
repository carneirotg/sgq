package net.sgq.incidentes.incidentes.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

public interface IncidenteEstadoController {

	@ApiOperation(value = "Altera o estado de um Incidente para 'Aberto'")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Estado alterado com sucesso"),
			@ApiResponse(code = 400, message = "Transição inválida"),
			@ApiResponse(code = 404, message = "Incidente não encontrado")})
	void incidenteMudarParaAberta(Long iId);

	@ApiOperation(value = "Altera o estado de um Incidente para 'Aberto'")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Estado alterado com sucesso"),
			@ApiResponse(code = 400, message = "Transição inválida"),
			@ApiResponse(code = 404, message = "Incidente não encontrado")})
	void incidenteMudarParaEmAnalise(Long iId);

	@ApiOperation(value = "Altera o estado de um Incidente para 'Aberto'")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Estado alterado com sucesso"),
			@ApiResponse(code = 400, message = "Transição inválida"),
			@ApiResponse(code = 404, message = "Incidente não encontrado")})
	void incidenteMudarParaConcluida(Long iId);
	
}
