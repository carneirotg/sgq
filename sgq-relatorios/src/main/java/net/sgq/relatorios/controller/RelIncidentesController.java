package net.sgq.relatorios.controller;

import java.util.Date;

public interface RelIncidentesController {

	Object incidentesMesAtual();
	
	Object incidentesSemestre();
	
	Object incidentesAnoCorrente();
	
	Object incidentesUltimosDozeMeses();
	
	Object incidentesPorPeriodo(Date inicio, Date fim);
	
}
