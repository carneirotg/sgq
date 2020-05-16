package net.sgq.transparencia.eventos.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import net.sgq.transparencia.clientes.to.IncidenteTO;
import net.sgq.transparencia.clientes.to.Incidentes;

public interface EventoController {

	ResponseEntity<List<IncidenteTO>> listaJson(Integer pagina, Integer registros);

	ResponseEntity<Incidentes> listaXml(Integer pagina, Integer registros);

}
