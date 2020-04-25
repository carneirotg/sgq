package net.sgq.relatorios.controller;

import java.util.Date;

import org.springframework.http.ResponseEntity;

public interface RelIncidentesController {

	ResponseEntity<byte[]> incidentesMesAtual();
	
	ResponseEntity<byte[]> incidentesSemestre();
	
	ResponseEntity<byte[]> incidentesAnoCorrente();
	
	ResponseEntity<byte[]> incidentesUltimosDozeMeses();
	
	ResponseEntity<byte[]> incidentesPorPeriodo(Date inicio, Date fim);
	
}
