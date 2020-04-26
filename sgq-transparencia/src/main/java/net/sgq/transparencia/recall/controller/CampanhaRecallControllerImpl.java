package net.sgq.transparencia.recall.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import net.sgq.transparencia.recall.modelos.enums.Estado;
import net.sgq.transparencia.recall.modelos.to.CampanhaRecallTO;
import net.sgq.transparencia.recall.servicos.CampanhaService;

@RestController
@RequestMapping("/campanhas")
public class CampanhaRecallControllerImpl implements CampanhaRecallController {

	@Autowired
	private CampanhaService service;
	
	@Override
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<CampanhaRecallTO>> todas() {
		return new ResponseEntity<>(service.buscar(null), HttpStatus.OK);
	}

	@Override
	@GetMapping(params = "estado=ativas",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<CampanhaRecallTO>> todasEmAndamento() {
		return trataRespostasBusca(Estado.ATIVA);
	}

	@Override
	@GetMapping(params = "estado=concluidas", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<CampanhaRecallTO>> todasConcluidas() {
		return trataRespostasBusca(Estado.CONCLUIDA);
	}

	@Override
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> novaCampanha(@RequestBody CampanhaRecallTO campanha) throws URISyntaxException {
		
		Long id = this.service.salvar(campanha);
		
		return ResponseEntity.created(new URI("/campanhas/" + id)).build();
	}

	@Override
	@PatchMapping("/{id}/fim/{novaDataTermino}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void atualizaDataTermino(@PathVariable Long id, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date novaDataTermino) {
		
		if(novaDataTermino.before(new Date())) {
			throw new IllegalStateException("Nova data de término deve futura.");
		}
		
		this.service.atualizaDataTermino(id, novaDataTermino);
	}

	@Override
	@PatchMapping("/{id}/estado/concluida")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void concluiCampanha(@PathVariable Long id) {
		this.service.concluiCampanha(id);
	}
	
	private ResponseEntity<List<CampanhaRecallTO>> trataRespostasBusca(Estado estado){
		List<CampanhaRecallTO> campanhasEstado = this.service.buscar(estado);
		
		if(campanhasEstado.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		return new ResponseEntity<>(campanhasEstado, HttpStatus.OK);
		
	}

}
