package net.sgq.transparencia.clientes;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.sgq.transparencia.clientes.to.IncidenteTO;

@FeignClient("gestao-incidentes")
public interface GestaoIncidentesClient {

	@GetMapping(path = "/v1/incidentes/?estado=concluidos&janelaMinutos")
	List<IncidenteTO> consultaIncidentesConcluidosJanela();

	@GetMapping(path = "/v1/incidentes/?estado=concluidos&descSort=true")
	@Cacheable(value = "incidentes")
	ResponseEntity<List<IncidenteTO>> consultaIncidentesConcluidos(
			@RequestParam(defaultValue = "1", required = false) Integer pagina,
			@RequestParam(defaultValue = "10", required = false) Integer registros);
}
