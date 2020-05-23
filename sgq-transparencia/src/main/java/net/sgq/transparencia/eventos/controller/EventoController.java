package net.sgq.transparencia.eventos.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.sgq.transparencia.clientes.to.IncidenteTO;
import net.sgq.transparencia.clientes.to.Incidentes;
import net.sgq.transparencia.recall.modelos.Campanhas;
import net.sgq.transparencia.recall.modelos.to.CampanhaRecallTO;

public interface EventoController {

	@ApiOperation(value = "Incidentes ocorridos no SGQ", response = IncidenteTO.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "JSON contendo incidentes") })
	ResponseEntity<List<IncidenteTO>> listaJsonIncidentes(Integer pagina, Integer registros);

	@ApiOperation(value = "Incidentes ocorridos no SGQ", response = Incidentes.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "XML contendo incidentes") })
	ResponseEntity<Incidentes> listaXmlIncidentes(Integer pagina, Integer registros);
	
	@ApiOperation(value = "Campanhas iniciadas no SGQ", response = CampanhaRecallTO.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "JSON contendo campanhas") })
	ResponseEntity<List<CampanhaRecallTO>> listaJsonCampanhas(Integer pagina, Integer registros);

	@ApiOperation(value = "Campanhas iniciadas no SGQ", response = Campanhas.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "XML contendo campanhas") })
	ResponseEntity<Campanhas> listaXmlCampanhas(Integer pagina, Integer registros);

}
