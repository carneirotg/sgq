package net.sgq.transparencia.eventos.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import net.sgq.transparencia.clientes.to.IncidenteTO;
import net.sgq.transparencia.clientes.to.Incidentes;
import net.sgq.transparencia.recall.modelos.Campanhas;
import net.sgq.transparencia.recall.modelos.to.CampanhaRecallTO;

public interface EventoController {

	ResponseEntity<List<IncidenteTO>> listaJsonIncidentes(Integer pagina, Integer registros);

	ResponseEntity<Incidentes> listaXmlIncidentes(Integer pagina, Integer registros);
	
	ResponseEntity<List<CampanhaRecallTO>> listaJsonCampanhas(Integer pagina, Integer registros);

	ResponseEntity<Campanhas> listaXmlCampanhas(Integer pagina, Integer registros);

}
