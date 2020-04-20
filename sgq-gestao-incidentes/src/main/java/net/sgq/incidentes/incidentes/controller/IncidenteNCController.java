package net.sgq.incidentes.incidentes.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

public interface IncidenteNCController {
	
	@ApiOperation(value = "Adiciona uma NC ao Incidente informado")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "NC associada com sucesso"),
			@ApiResponse(code = 404, message = "Entidade(s) não encontrada(s)")})
	void adicionaNaoConformidade(Long iId, Long ncId);
	
	@ApiOperation(value = "Remove uma NC do Incidente informado")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "NC removida com sucesso"),
			@ApiResponse(code = 404, message = "Entidade(s) não encontrada(s)")})
	void removeNaoConformidade(Long iId, Long ncId);
	
	@ApiOperation(value = "Remove todas NCs do Incidente informado")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "NCs removidas com sucesso"),
			@ApiResponse(code = 404, message = "Incidente não encontrado")})
	void removeTodasNCs(Long iId);
	
}
