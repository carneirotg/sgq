package net.sgq.incidentes.incidentes.controller;

import java.net.URISyntaxException;
import java.util.List;

import org.springframework.http.ResponseEntity;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.sgq.incidentes.incidentes.modelos.Incidente;

public interface IncidenteController {

	@ApiOperation(value = "Consulta um incidente por identificador", response = Incidente.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Incidente retornado com sucesso"),
			@ApiResponse(code = 404, message = "Incidente não encontrado") })
	ResponseEntity<Incidente> buscaPor(Long id);

	@ApiOperation(value = "Consulta incidentes por nome", response = Incidente.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Incidentes retornados com sucesso"),
			@ApiResponse(code = 404, message = "Nenhum incidente encontrado") })
	ResponseEntity<List<Incidente>> buscaPor(String descricao, Integer pagina, Integer registros);

	@ApiOperation(value = "Consulta incidentes por nome", response = Incidente.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Incidentes retornados com sucesso"),
			@ApiResponse(code = 404, message = "Nenhum incidente encontrado") })
	ResponseEntity<List<Incidente>> listaIncidentes(Integer pagina, Integer registros);

	@ApiOperation(value = "Consulta incidentes com estado 'Aberto'", response = Incidente.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Incidentes retornados com sucesso"),
			@ApiResponse(code = 404, message = "Nenhum incidente encontrado") })
	ResponseEntity<List<Incidente>> incidentesAbertos(Integer pagina, Integer registros);

	@ApiOperation(value = "Consulta incidentes com estado 'Em Análise'", response = Incidente.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Incidentes retornados com sucesso"),
			@ApiResponse(code = 404, message = "Nenhum incidente encontrado") })
	ResponseEntity<List<Incidente>> incidentesEmAnalise(Integer pagina, Integer registros);

	@ApiOperation(value = "Consulta incidentes com estado 'Concluído'", response = Incidente.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Incidentes retornados com sucesso"),
			@ApiResponse(code = 404, message = "Nenhum incidente encontrado") })
	ResponseEntity<List<Incidente>> incidentesConcluidos(Integer pagina, Integer registros);

	@ApiOperation(value = "Consulta incidentes com estado 'Concluído'", response = Incidente.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Incidentes retornados com sucesso"),
			@ApiResponse(code = 404, message = "Nenhum incidente encontrado") })
	ResponseEntity<List<Incidente>> incidentesConcluidosJanela(Integer janelaMinutos, Integer pagina,
			Integer registros);

	@ApiOperation(value = "Consulta incidentes ainda não concluídos", response = Incidente.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Incidentes retornados com sucesso"),
			@ApiResponse(code = 404, message = "Nenhum incidente encontrado") })
	ResponseEntity<List<Incidente>> incidentesNaoConcluidos(Integer pagina, Integer registros);

	@ApiOperation(value = "Cria um novo Incidente")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Incidente criado com sucesso") })
	ResponseEntity<Void> novoIncidente(Incidente incidente) throws URISyntaxException;

	@ApiOperation(value = "Atualiza um incidente")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Incidente atualizado com sucesso"),
			@ApiResponse(code = 201, message = "Incidente atualizado com sucesso") })
	void atualizaIncidente(Long id, Incidente incidente);

}
