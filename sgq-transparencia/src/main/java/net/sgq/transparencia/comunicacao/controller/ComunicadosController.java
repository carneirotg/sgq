package net.sgq.transparencia.comunicacao.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.sgq.transparencia.comunicacao.modelos.Envio;

public interface ComunicadosController {

	@ApiOperation(value = "Lista os eventos comunicados pelo sistema com partes interessadas", response = Envio.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Lista de envios realizados") })
	ResponseEntity<List<Envio>> todos(Integer pagina, Integer registros);
	
}
