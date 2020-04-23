package net.sgq.transparencia.comunicacao.servicos.agendador;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import net.sgq.transparencia.clientes.GestaoIncidentesClient;
import net.sgq.transparencia.clientes.to.IncidenteTO;
import net.sgq.transparencia.comunicacao.servicos.ComunicacaoService;
import net.sgq.transparencia.comunicacao.servicos.DestinatarioService;

@Component
public class ComunicacaoScheduler {

	@Autowired
	private GestaoIncidentesClient incidentesClient;

	@Autowired
	private DestinatarioService destinatarioService;

	@Autowired
	private ComunicacaoService comunicacaoService;

	private Logger logger = LoggerFactory.getLogger(ComunicacaoScheduler.class);

	@Scheduled(fixedDelay = 25000)
	public void enviaComunicacaoIncidentes() {

		List<IncidenteTO> incidentes = new ArrayList<>();
		
		try {
			incidentes = incidentesClient.consultaIncidentesConcluidos();
		} catch(EntityNotFoundException e) {
			logger.info("Sem informações de incidentes disponíveis");
		}

		if (!incidentes.isEmpty()) {
			logger.info("Novo(s) incidente(s) a comunicar: {}", incidentes.size());

			comunicacaoService.pushDeEventos(destinatarioService.todos().stream().filter(d -> d.getAssinanteEventos())
					.collect(Collectors.toList()), incidentes);
		}

	}

}
