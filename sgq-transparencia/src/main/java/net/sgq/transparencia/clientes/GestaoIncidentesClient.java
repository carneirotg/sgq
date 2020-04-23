package net.sgq.transparencia.clientes;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import net.sgq.transparencia.clientes.to.IncidenteTO;

@FeignClient("gestao-incidentes")
public interface GestaoIncidentesClient {

	@RequestMapping(method = RequestMethod.GET, path = "/v1/incidentes/?estado=concluidos")
	@JacksonXmlElementWrapper(useWrapping = true)
	@JacksonXmlProperty(localName = "Incidente")
	List<IncidenteTO> consultaIncidentesConcluidos();
	
}
