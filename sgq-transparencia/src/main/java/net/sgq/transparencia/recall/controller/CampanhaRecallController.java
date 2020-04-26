package net.sgq.transparencia.recall.controller;

import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;

import org.springframework.http.ResponseEntity;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.sgq.transparencia.recall.modelos.to.CampanhaRecallTO;

public interface CampanhaRecallController {
	
	@ApiOperation(value = "Lista as campanhas de recall presentes no sistema", response = CampanhaRecallTO.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Campanhas listadas com sucesso") })
	ResponseEntity<List<CampanhaRecallTO>> todas();
	
	@ApiOperation(value = "Lista as campanhas de recall presentes no sistema que se encontram em andamento", response = CampanhaRecallTO.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Campanhas listadas com sucesso") })
	ResponseEntity<List<CampanhaRecallTO>> todasEmAndamento();
	
	@ApiOperation(value = "Lista as campanhas de recall presentes no sistema que já foram concluídas", response = CampanhaRecallTO.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Campanhas listadas com sucesso") })
	ResponseEntity<List<CampanhaRecallTO>> todasConcluidas();
	
	
	@ApiOperation(value = "Cria uma nova campanha de recall")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Campanha criada com sucesso") })
	ResponseEntity<Void> novaCampanha(CampanhaRecallTO campanha) throws URISyntaxException;
	
	@ApiOperation(value = "Atualiza uma campanha de recall com uma nova data de término")
	@ApiResponses(value = { @ApiResponse(code = 204, message = "Data atualizada com sucesso") })
	void atualizaDataTermino(Long id, Date novaDataTermino);
	
	@ApiOperation(value = "Conclui uma campanha de recall")
	@ApiResponses(value = { @ApiResponse(code = 204, message = "Campanha concluída com sucesso") })
	void concluiCampanha(Long id);
	
}