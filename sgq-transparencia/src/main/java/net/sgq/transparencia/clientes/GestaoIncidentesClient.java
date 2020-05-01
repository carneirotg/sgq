package net.sgq.transparencia.clientes;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import net.sgq.transparencia.clientes.to.IncidenteTO;

@FeignClient("gestao-incidentes")
public interface GestaoIncidentesClient {

	@GetMapping(path = "/v1/incidentes/?estado=concluidos&janelaMinutos")
	@JacksonXmlElementWrapper(useWrapping = true)
	@JacksonXmlProperty(localName = "Incidente")
	List<IncidenteTO> consultaIncidentesConcluidos();
	
}
