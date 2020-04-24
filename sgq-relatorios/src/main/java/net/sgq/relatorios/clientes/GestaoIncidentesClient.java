package net.sgq.relatorios.clientes;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.sgq.relatorios.clientes.to.IncidenteTO;

@FeignClient("gestao-incidentes")
public interface GestaoIncidentesClient {

	@RequestMapping(method = RequestMethod.GET, path = "/v1/incidentes")
	List<IncidenteTO> consultaIncidentesConcluidos();
	
}
