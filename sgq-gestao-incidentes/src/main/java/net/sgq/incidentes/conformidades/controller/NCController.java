package net.sgq.incidentes.conformidades.controller;

import java.net.URISyntaxException;
import java.util.List;

import org.springframework.http.ResponseEntity;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.sgq.incidentes.conformidades.modelos.to.NaoConformidadeIdTO;
import net.sgq.incidentes.conformidades.modelos.to.NaoConformidadeTO;

public interface NCController {
	@ApiOperation(value = "Lista NCs presentes no sistema", response = NaoConformidadeIdTO.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Norma associada com sucesso") })
	ResponseEntity<List<NaoConformidadeIdTO>> listaNCs();

	@ApiOperation(value = "Lista NCs presentes no sistema por nome", response = NaoConformidadeIdTO.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Norma associada com sucesso"),
			@ApiResponse(code = 404, message = "Nenhuma norma com nome informado") })
	ResponseEntity<List<NaoConformidadeIdTO>> consultaNC(String nome);

	@ApiOperation(value = "Consulta uma NC por seu identificador", response = NaoConformidadeIdTO.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Norma associada com sucesso"),
			@ApiResponse(code = 404, message = "Norma inexistente") })
	ResponseEntity<NaoConformidadeIdTO> consultaNC(Long id);

	@ApiOperation(value = "Cria uma nova NC")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "NC criada com sucesso") })
	ResponseEntity<Void> novaNC(NaoConformidadeTO naoConformidadeTo) throws URISyntaxException;

	@ApiOperation(value = "Atualiza uma NC")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "NC atualizada com sucesso") })
	void atualizaNC(Long id, NaoConformidadeTO naoConformidadeTo);

	@ApiOperation(value = "Lista NCs com situação 'Aberta'")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Listadas NCs encontradas "),
			@ApiResponse(code = 404, message = "Nenhuma NC nessa situação encontrada") })
	ResponseEntity<List<NaoConformidadeIdTO>> nCsAbertas();

	@ApiOperation(value = "Lista NCs com situação 'Em Análise'")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Listadas NCs encontradas "),
			@ApiResponse(code = 404, message = "Nenhuma NC nessa situação encontrada") })
	ResponseEntity<List<NaoConformidadeIdTO>> nCsEmAnalise();

	@ApiOperation(value = "Lista NCs com situação 'Concluída'")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Listadas NCs encontradas "),
			@ApiResponse(code = 404, message = "Nenhuma NC nessa situação encontrada") })
	ResponseEntity<List<NaoConformidadeIdTO>> nCsConcluidas();
	
	@ApiOperation(value = "Lista NCs não concluídas")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Listadas NCs encontradas "),
			@ApiResponse(code = 404, message = "Nenhuma NC nessa situação encontrada") })
	ResponseEntity<List<NaoConformidadeIdTO>> nCsNaoConcluidas();

}
