package net.sgq.relatorios.servicos;

import java.util.Date;

import net.sgq.relatorios.modelos.enums.Periodo;

public interface RelIncidentesService {

	Object geraRelatorioPor(Periodo periodo);
	Object geraRelatorioPor(Date inicio, Date fim);
	
}
