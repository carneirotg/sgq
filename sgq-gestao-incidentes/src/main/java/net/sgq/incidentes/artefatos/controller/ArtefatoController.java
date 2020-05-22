package net.sgq.incidentes.artefatos.controller;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.sgq.incidentes.artefatos.modelos.Artefato;

public interface ArtefatoController {

	@ApiOperation(value = "Listagem dos artefatos existente no sistema", response = Artefato.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lista de artefatos retornada") })
	ResponseEntity<List<Artefato>> todos(Integer pagina, Integer registros); 
	
	@ApiOperation(value = "Busca um artefato existente no sistema por consulta de identificador", response = Artefato.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lista de artefatos retornada") })
	ResponseEntity<Artefato> buscaArtefatoPor(Long id);
	
	@ApiOperation(value = "Listagem dos artefatos existente no sistema por consulta de nome", response = Artefato.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lista de artefatos retornada") })
	ResponseEntity<List<Artefato>> buscaArtefatosPor(String nome, Integer pagina, Integer registros);
	
	
	@ApiOperation(value = "Listagem dos artefatos existente no sistema", response = Void.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Artefato criado com sucesso") })
	ResponseEntity<Void> criaArtefato(Artefato artefatoTO) throws URISyntaxException;
	
	@ApiOperation(value = "Atualiza um artefato existente no sistema que não esteja depreciado", response = Void.class)
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Artefato atualizado com sucesso") })
	void atualizaArtefato(Long id, Artefato artefatoIdTO);
	
	@ApiOperation(value = "Deprecia um artefato existente no sistema para que não seja mais utilizado", response = Void.class)
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Artefato depreciado com sucesso") })
	void depreciaArtefato(Long id);
	
	Map<String, Long> estatisticas();
	
}
