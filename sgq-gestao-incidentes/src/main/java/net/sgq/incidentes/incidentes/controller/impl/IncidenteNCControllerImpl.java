package net.sgq.incidentes.incidentes.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import net.sgq.incidentes.incidentes.controller.IncidenteNCController;
import net.sgq.incidentes.incidentes.servicos.IncidenteService;

@RestController
@RequestMapping("/incidentes/{iId}/ncs")
public class IncidenteNCControllerImpl implements IncidenteNCController {

	@Autowired
	private IncidenteService service;
	
	@Override
	@PatchMapping("/{ncId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void adicionaNaoConformidade(@PathVariable Long iId, @PathVariable Long ncId) {
		this.service.adicionaNaoConformidade(iId, ncId);		
	}

	@Override
	@DeleteMapping("/{ncId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removeNaoConformidade(@PathVariable Long iId, @PathVariable Long ncId) {
		this.service.removeNaoConformidade(iId, ncId);
	}

	@Override
	@DeleteMapping
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removeTodasNCs(@PathVariable Long iId) {
		this.service.removeTodasNaoConformidades(iId);
	}

	
	
}
