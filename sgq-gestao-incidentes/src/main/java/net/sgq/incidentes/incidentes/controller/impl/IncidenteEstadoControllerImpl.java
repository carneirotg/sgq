package net.sgq.incidentes.incidentes.controller.impl;

import static org.springframework.http.HttpStatus.NO_CONTENT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import net.sgq.incidentes.conformidades.modelos.enums.Estado;
import net.sgq.incidentes.incidentes.controller.IncidenteEstadoController;
import net.sgq.incidentes.incidentes.servicos.IncidenteService;

@RestController
@RequestMapping("/incidentes/{iId}/estado")
public class IncidenteEstadoControllerImpl implements IncidenteEstadoController {

	@Autowired
	private IncidenteService service;

	@Override
	@PatchMapping("/aberto")
	@ResponseStatus(code = NO_CONTENT)
	public void incidenteMudarParaAberta(@PathVariable Long iId) {
		this.service.incidenteMudaEstado(iId, Estado.ABERTA);
	}

	@Override
	@PatchMapping("/em_analise")
	@ResponseStatus(code = NO_CONTENT)
	public void incidenteMudarParaEmAnalise(@PathVariable Long iId) {
		this.service.incidenteMudaEstado(iId, Estado.EM_ANALISE);
	}

	@Override
	@PatchMapping("/concluido")
	@Secured({ "ROLE_GESTOR" })
	@ResponseStatus(code = NO_CONTENT)
	public void incidenteMudarParaConcluida(@PathVariable Long iId) {
		this.service.incidenteMudaEstado(iId, Estado.CONCLUIDA);
	}

}
