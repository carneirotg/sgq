package net.sgq.relatorios.servicos;

import java.io.File;
import java.util.Date;

import net.sgq.relatorios.modelos.enums.Periodo;

public interface RelIncidentesService {

	File geraRelatorioPor(Periodo periodo);
	File geraRelatorioPor(Date inicio, Date fim);
	
}
