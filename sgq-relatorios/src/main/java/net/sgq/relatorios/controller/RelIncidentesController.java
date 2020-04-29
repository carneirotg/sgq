package net.sgq.relatorios.controller;

import java.util.Date;

import org.springframework.http.ResponseEntity;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

public interface RelIncidentesController {

	@ApiOperation(value = "Relatório de incidentes do mês atual", response = byte[].class, responseContainer = "Pdf")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Relatório gerado com sucesso") })
	ResponseEntity<byte[]> incidentesMesAtual();
	
	@ApiOperation(value = "Relatório de incidentes dos últimos seis meses", response = byte[].class, responseContainer = "Pdf")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Relatório gerado com sucesso") })
	ResponseEntity<byte[]> incidentesSemestre();
	
	@ApiOperation(value = "Relatório de incidentes do ano atual", response = byte[].class, responseContainer = "Pdf")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Relatório gerado com sucesso") })
	ResponseEntity<byte[]> incidentesAnoCorrente();
	
	@ApiOperation(value = "Relatório de incidentes dos últimos doze meses", response = byte[].class, responseContainer = "Pdf")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Relatório gerado com sucesso") })
	ResponseEntity<byte[]> incidentesUltimosDozeMeses();
	
	@ApiOperation(value = "Relatório de incidentes do período apontado", response = byte[].class, responseContainer = "Pdf")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Relatório gerado com sucesso") })
	ResponseEntity<byte[]> incidentesPorPeriodo(Date inicio, Date fim);
	
}
