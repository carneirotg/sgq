package net.sgq.incidentes.clientes;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import net.sgq.incidentes.conformidades.modelos.Norma;

@FeignClient("gw-normas")
public interface GestaoNormasClient {

	@GetMapping("/v1/normas/{normaId}")
	Norma consultaNorma(@PathVariable Long normaId);

}
