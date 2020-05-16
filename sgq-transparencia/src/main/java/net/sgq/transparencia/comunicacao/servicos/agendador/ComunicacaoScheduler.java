package net.sgq.transparencia.comunicacao.servicos.agendador;

import java.util.ArrayList;
import java.util.List;

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
import net.sgq.transparencia.recall.modelos.enums.Estado;
import net.sgq.transparencia.recall.modelos.to.CampanhaRecallTO;
import net.sgq.transparencia.recall.servicos.CampanhaService;

@Component
public class ComunicacaoScheduler {

	@Autowired
	private GestaoIncidentesClient incidentesClient;

	@Autowired
	private DestinatarioService destinatarioService;

	@Autowired
	private CampanhaService campanhaService;

	@Autowired
	private ComunicacaoService comunicacaoService;

	private Logger logger = LoggerFactory.getLogger(ComunicacaoScheduler.class);

	@Scheduled(fixedDelay = 25000)
	public void enviaComunicacaoIncidentes() {

		List<IncidenteTO> incidentes = new ArrayList<>();

		try {
			incidentes = incidentesClient.consultaIncidentesConcluidosJanela();
		} catch (EntityNotFoundException e) {
			logger.info("Sem informações de incidentes disponíveis");
		}

		if (!incidentes.isEmpty()) {
			logger.info("Novo(s) incidente(s) a comunicar: {}", incidentes.size());

			comunicacaoService.pushDeEventos(destinatarioService.interessadosIncidentes(), incidentes);
		}

	}

	@Scheduled(fixedDelay = 30000)
	public void enviaComunicacaoRecalls() {

		List<CampanhaRecallTO> recalls = campanhaService.buscar(Estado.ATIVA, 30);

		if (recalls.isEmpty()) {
			logger.info("Sem informações de recalls ativos disponíveis");
		} else {
			logger.info("Novo(s) Recall(s) a comunicar: {}", recalls.size());

			comunicacaoService.pushDeEventos(destinatarioService.interessadosRecall(), recalls);
		}

	}

}
