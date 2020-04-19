package net.sgq.incidentes.conformidades;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.sgq.incidentes.conformidades.modelos.Norma;

@FeignClient("gw-normas")
public interface GestaoNormasClient {

	@RequestMapping(method = RequestMethod.GET, path = "/normas/{normaId}")
	Norma consultaNorma(@PathVariable Long normaId);

}
