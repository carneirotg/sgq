package net.sgq.incidentes.conformidades.controller;

import java.net.URISyntaxException;
import java.util.List;

import org.springframework.http.ResponseEntity;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.sgq.incidentes.conformidades.modelos.NaoConformidade;

public interface NCController {
	@ApiOperation(value = "Lista NCs presentes no sistema", response = NaoConformidade.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Norma associada com sucesso") })
	ResponseEntity<List<NaoConformidade>> listaNCs(Integer paginas, Integer registros);

	@ApiOperation(value = "Lista NCs presentes no sistema por nome", response = NaoConformidade.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Norma associada com sucesso"),
			@ApiResponse(code = 404, message = "Nenhuma norma com nome informado") })
	ResponseEntity<List<NaoConformidade>> consultaNC(String nome, Integer paginas, Integer registros);

	@ApiOperation(value = "Consulta uma NC por seu identificador", response = NaoConformidade.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Norma associada com sucesso"),
			@ApiResponse(code = 404, message = "Norma inexistente") })
	ResponseEntity<NaoConformidade> consultaNC(Long id);

	@ApiOperation(value = "Cria uma nova NC")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "NC criada com sucesso") })
	ResponseEntity<Void> novaNC(NaoConformidade naoConformidadeTo) throws URISyntaxException;

	@ApiOperation(value = "Atualiza uma NC")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "NC atualizada com sucesso") })
	void atualizaNC(Long id, NaoConformidade naoConformidadeTo);

	@ApiOperation(value = "Lista NCs com situação 'Aberta'")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Listadas NCs encontradas "),
			@ApiResponse(code = 404, message = "Nenhuma NC nessa situação encontrada") })
	ResponseEntity<List<NaoConformidade>> nCsAbertas(Integer paginas, Integer registros);

	@ApiOperation(value = "Lista NCs com situação 'Em Análise'")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Listadas NCs encontradas "),
			@ApiResponse(code = 404, message = "Nenhuma NC nessa situação encontrada") })
	ResponseEntity<List<NaoConformidade>> nCsEmAnalise(Integer paginas, Integer registros);

	@ApiOperation(value = "Lista NCs com situação 'Concluída'")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Listadas NCs encontradas "),
			@ApiResponse(code = 404, message = "Nenhuma NC nessa situação encontrada") })
	ResponseEntity<List<NaoConformidade>> nCsConcluidas(Integer paginas, Integer registros);

	@ApiOperation(value = "Lista NCs não concluídas")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Listadas NCs encontradas "),
			@ApiResponse(code = 404, message = "Nenhuma NC nessa situação encontrada") })
	ResponseEntity<List<NaoConformidade>> nCsNaoConcluidas(Integer paginas, Integer registros);

}
