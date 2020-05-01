package net.sgq.transparencia.comunicacao.servicos.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import net.sgq.transparencia.comunicacao.modelos.Comunicado;
import net.sgq.transparencia.comunicacao.modelos.to.DestinatarioTO;
import net.sgq.transparencia.comunicacao.servicos.ComunicacaoService;

@Service
public class ComunicacaoServiceImpl implements ComunicacaoService {

	private Logger logger = LoggerFactory.getLogger(ComunicacaoServiceImpl.class);

	@Override
	public void pushDeEventos(List<DestinatarioTO> destinatarios, List<? extends Comunicado> comunicados) {

		for (DestinatarioTO destinatario : destinatarios) {
			for (Comunicado comunicado : comunicados) {
				logger.info("Push enviado para '{}': {}", destinatario.getDescricao(), comunicado);
			}
		}

	}

}
