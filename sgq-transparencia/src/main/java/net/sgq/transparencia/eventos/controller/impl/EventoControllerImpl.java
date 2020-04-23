package net.sgq.transparencia.eventos.controller.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.sgq.transparencia.clientes.GestaoIncidentesClient;
import net.sgq.transparencia.clientes.to.IncidenteTO;
import net.sgq.transparencia.clientes.to.Incidentes;
import net.sgq.transparencia.eventos.controller.EventoController;

@RestController
@RequestMapping("/eventos")
public class EventoControllerImpl implements EventoController {

	@Autowired
	private GestaoIncidentesClient client;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<IncidenteTO> listaJson() {
		return this.client.consultaIncidentesConcluidos();
	}

	@GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
	public Incidentes listaXml() {
		return new Incidentes(this.client.consultaIncidentesConcluidos());
	}

}
