package net.sgq.incidentes.conformidades.controller.impl;

import static org.springframework.http.HttpStatus.NO_CONTENT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import net.sgq.incidentes.conformidades.controller.NCEstadoController;
import net.sgq.incidentes.conformidades.modelos.enums.Estado;
import net.sgq.incidentes.conformidades.servicos.NaoConformidadeService;

@RestController
@RequestMapping("/ncs/{ncId}/estado/")
public class NCEstadoControllerImpl implements NCEstadoController {

	@Autowired
	private NaoConformidadeService service;
	
	@Override
	@PatchMapping("/aberta")
	@ResponseStatus(code = NO_CONTENT)
	public void nCMudarParaAberta(@PathVariable Long ncId) {
		this.service.naoConformidadeMudaEstado(ncId, Estado.ABERTA);
		
	}

	@Override
	@PatchMapping("/em_analise")
	@ResponseStatus(code = NO_CONTENT)
	public void nCMudarParaEmAnalise(@PathVariable Long ncId) {
		this.service.naoConformidadeMudaEstado(ncId, Estado.EM_ANALISE);
		
	}

	@Override
	@PatchMapping("/concluida")
	@ResponseStatus(code = NO_CONTENT)
	public void nCMudarParaConcluida(@PathVariable Long ncId) {
		this.service.naoConformidadeMudaEstado(ncId, Estado.CONCLUIDA);
	}
	
}
