package net.sgq.incidentes.conformidades.controller.impl;

import static org.springframework.http.HttpStatus.NO_CONTENT;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import net.sgq.incidentes.conformidades.controller.NCNormaController;
import net.sgq.incidentes.conformidades.modelos.Norma;
import net.sgq.incidentes.conformidades.servicos.NaoConformidadeService;

@RestController
@RequestMapping("/ncs/{ncId}")
public class NCNormasControllerImpl implements NCNormaController {
	
	@Autowired
	private NaoConformidadeService service;

	@Override
	@PatchMapping("/norma/{normaId}")
	@ResponseStatus(code = NO_CONTENT)
	public void associarNorma(@PathVariable Long ncId, @PathVariable Long normaId, @AuthenticationPrincipal OAuth2Authentication usuario) {
		this.service.associaNCANorma(ncId, normaId, usuario);
	}
	
	@Override
	@GetMapping("/norma/{normaId}")
	public ResponseEntity<Norma> buscaNormaNC(@PathVariable Long ncId) {
		Norma norma = this.service.consultaNC(ncId).getNormaNaoConformidade();
		
		if(norma == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(norma, HttpStatus.OK);
	}
	
	@Override
	@PatchMapping("/norma/checklist")
	@ResponseStatus(code = NO_CONTENT)
	public void ncAtualizaChecklist(@PathVariable Long ncId, @RequestBody Map<String, Boolean> checklist) {
		this.service.atualizaChecklist(ncId, checklist);
	}
	
}
