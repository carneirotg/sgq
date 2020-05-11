package net.sgq.incidentes.conformidades.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.sgq.incidentes.conformidades.modelos.Norma;

public interface NCNormaController {

	@ApiOperation(value = "Associa uma norma à NC informada")
	@ApiResponses(value = { @ApiResponse(code = 204, message = "Norma associada com sucesso"),
			@ApiResponse(code = 400, message = "Transição inválida"),
			@ApiResponse(code = 404, message = "NC não encontrada") })
	void associarNorma(Long ncId, Long normaId, OAuth2Authentication usuario);

	@ApiOperation(value = "Consulta uma norma associada à NC informada", response = Norma.class)
	@ApiResponses(value = { @ApiResponse(code = 204, message = "Norma retornada com sucesso"),
			@ApiResponse(code = 404, message = "NC não encontrada") })
	ResponseEntity<Norma> buscaNormaNC(Long ncId);

	@ApiOperation(value = "Atualiza a checklist de norma associada à NC informada")
	@ApiResponses(value = { @ApiResponse(code = 204, message = "Norma associada com sucesso"),
			@ApiResponse(code = 204, message = "Norma retornada com sucesso"),
			@ApiResponse(code = 404, message = "NC não encontrada") })
	void ncAtualizaChecklist(Long ncId, Map<String, Boolean> checklist);

}