package net.sgq.incidentes.incidentes.controller;

import java.net.URISyntaxException;
import java.util.List;

import org.springframework.http.ResponseEntity;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.sgq.incidentes.incidentes.modelos.to.IncidenteIdTO;
import net.sgq.incidentes.incidentes.modelos.to.IncidenteTO;

public interface IncidenteController {

	@ApiOperation(value = "Consulta um incidente por identificador", response = IncidenteIdTO.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Incidente retornado com sucesso"),
			@ApiResponse(code = 404, message = "Incidente não encontrado") })
	ResponseEntity<IncidenteIdTO> buscaPor(Long id);

	@ApiOperation(value = "Consulta incidentes por nome", response = IncidenteIdTO.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Incidentes retornados com sucesso"),
			@ApiResponse(code = 404, message = "Nenhum incidente encontrado") })
	ResponseEntity<List<IncidenteIdTO>> buscaPor(String descricao);

	@ApiOperation(value = "Consulta incidentes por nome", response = IncidenteIdTO.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Incidentes retornados com sucesso"),
			@ApiResponse(code = 404, message = "Nenhum incidente encontrado") })
	ResponseEntity<List<IncidenteIdTO>> listaIncidentes();

	@ApiOperation(value = "Consulta incidentes com estado 'Aberto'", response = IncidenteIdTO.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Incidentes retornados com sucesso"),
			@ApiResponse(code = 404, message = "Nenhum incidente encontrado") })
	ResponseEntity<List<IncidenteIdTO>> incidentesAbertos();

	@ApiOperation(value = "Consulta incidentes com estado 'Em Análise'", response = IncidenteIdTO.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Incidentes retornados com sucesso"),
			@ApiResponse(code = 404, message = "Nenhum incidente encontrado") })
	ResponseEntity<List<IncidenteIdTO>> incidentesEmAnalise();

	@ApiOperation(value = "Consulta incidentes com estado 'Concluído'", response = IncidenteIdTO.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Incidentes retornados com sucesso"),
			@ApiResponse(code = 404, message = "Nenhum incidente encontrado") })
	ResponseEntity<List<IncidenteIdTO>> incidentesConcluidos();
	
	@ApiOperation(value = "Consulta incidentes com estado 'Concluído'", response = IncidenteIdTO.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Incidentes retornados com sucesso"),
			@ApiResponse(code = 404, message = "Nenhum incidente encontrado") })
	ResponseEntity<List<IncidenteIdTO>> incidentesConcluidosJanela(Integer janelaMinutos);
	
	@ApiOperation(value = "Consulta incidentes ainda não concluídos", response = IncidenteIdTO.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Incidentes retornados com sucesso"),
			@ApiResponse(code = 404, message = "Nenhum incidente encontrado") })
	ResponseEntity<List<IncidenteIdTO>> incidentesNaoConcluidos();

	@ApiOperation(value = "Cria um novo Incidente")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Incidente criado com sucesso") })
	ResponseEntity<Void> novoIncidente(IncidenteTO incidente) throws URISyntaxException;

	@ApiOperation(value = "Atualiza um incidente")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Incidente atualizado com sucesso"),
			@ApiResponse(code = 201, message = "Incidente atualizado com sucesso") })
	void atualizaIncidente(Long id, IncidenteTO incidente);

}
