package net.sgq.gateway.normas.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.sgq.gateway.normas.modelos.Norma;

public interface NormaController {

	@ApiOperation(value = "Busca no módulo externo de Gestão de Normas todas as normas que estiverem disponíveis", response = Norma.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Lista de normas"),
			@ApiResponse(code = 404, message = "Nenhuma norma encontrada"),
			@ApiResponse(code = 502, message = "Sistema fora do ar") })
	ResponseEntity<List<Norma>> listaNormas();

	@ApiOperation(value = "Busca no módulo externo de Gestão de Normas uma norma por seu identificador", response = Norma.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Norma retornada com sucesso"),
			@ApiResponse(code = 404, message = "Nenhuma norma encontrada"),
			@ApiResponse(code = 502, message = "Sistema fora do ar") })
	ResponseEntity<Norma> consultaNorma(Long id);

}