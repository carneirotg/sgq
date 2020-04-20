package net.sgq.incidentes.conformidades.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

public interface NCEstadoController {

	@ApiOperation(value = "Altera o estado de uma NC para 'Aberta'")
	@ApiResponses(value = { @ApiResponse(code = 204, message = "Estado alterado com sucesso"),
			@ApiResponse(code = 400, message = "Transição inválida"),
			@ApiResponse(code = 404, message = "NC não encontrada") })
	void nCMudarParaAberta(Long ncId);

	@ApiOperation(value = "Altera o estado de uma NC para 'Em Análise'")
	@ApiResponses(value = { @ApiResponse(code = 204, message = "Estado alterado com sucesso"),
			@ApiResponse(code = 400, message = "Transição inválida"),
			@ApiResponse(code = 404, message = "NC não encontrada") })
	void nCMudarParaEmAnalise(Long ncId);

	@ApiOperation(value = "Altera o estado de uma NC para 'Concluída'")
	@ApiResponses(value = { @ApiResponse(code = 204, message = "Estado alterado com sucesso"),
			@ApiResponse(code = 400, message = "Transição inválida"),
			@ApiResponse(code = 404, message = "NC não encontrada") })
	void nCMudarParaConcluida(Long ncId);

}