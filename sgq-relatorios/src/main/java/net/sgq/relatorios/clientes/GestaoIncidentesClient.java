package net.sgq.relatorios.clientes;

import java.util.Date;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.sgq.relatorios.clientes.to.IncidenteTO;

@FeignClient("gestao-incidentes")
public interface GestaoIncidentesClient {

	@RequestMapping(method = RequestMethod.GET, path = "/v1/incidentes")
	List<IncidenteTO> consultaIncidentesConcluidos();

	@RequestMapping(method = RequestMethod.GET, path = "/v1/incidentes/de/{inicio}/ate/{fim}")
	List<IncidenteTO> consultaIncidentesPorPeriodo(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date inicio,
			@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date fim);

}
