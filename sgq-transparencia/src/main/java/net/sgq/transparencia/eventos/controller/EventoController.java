package net.sgq.transparencia.eventos.controller;

import java.util.List;

import net.sgq.transparencia.clientes.to.IncidenteTO;
import net.sgq.transparencia.clientes.to.Incidentes;

public interface EventoController {

	List<IncidenteTO> listaJson();
	Incidentes listaXml();
	
}
