package net.sgq.transparencia.comunicacao.controller.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.sgq.transparencia.comunicacao.controller.ComunicadosController;
import net.sgq.transparencia.comunicacao.modelos.Envio;
import net.sgq.transparencia.comunicacao.modelos.EnvioRepository;
import net.sgq.transparencia.utils.handler.PageHeaders;

@RestController
@RequestMapping("/comunicados")
public class ComunicadosControllerImpl implements ComunicadosController {

	@Autowired
	private EnvioRepository repo;

	@Override
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@Cacheable(value = "comunicadosEnviados")
	public ResponseEntity<List<Envio>> todos(@RequestParam(defaultValue = "1", required = false) Integer pagina,
			@RequestParam(defaultValue = "10", required = false) Integer registros) {

		Page<Envio> comunicadosEnviados = repo.findAll(PageRequest.of(pagina - 1, registros, Sort.by(Direction.DESC, "id")));

		if (comunicadosEnviados.getContent().isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(comunicadosEnviados.getContent(), PageHeaders.headers(comunicadosEnviados), HttpStatus.OK);
	}

}
