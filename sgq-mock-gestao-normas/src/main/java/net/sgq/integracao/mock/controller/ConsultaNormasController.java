package net.sgq.integracao.mock.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.sgq.integracao.mock.model.Norma;

@Api(value = "API Gestão de Normas")
public interface ConsultaNormasController {

	@ApiOperation(value = "Listagem das normas disponíveis no acervo para consulta", response = Norma.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lista de normas retornada") })
	ResponseEntity<List<Norma>> listaNormas();

	@ApiOperation(value = "Consulta uma norma e seus detalhes de acordo com o identificador fornecido", response = Norma.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Norma encontrada e retornada"),
			@ApiResponse(code = 404, message = "Norma não encontrada") })
	ResponseEntity<Norma> consultaNorma(Long id);
}
