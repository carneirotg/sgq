package net.sgq.transparencia.comunicacao.servicos.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import net.sgq.transparencia.comunicacao.modelos.Comunicado;
import net.sgq.transparencia.comunicacao.modelos.Envio;
import net.sgq.transparencia.comunicacao.modelos.EnvioRepository;
import net.sgq.transparencia.comunicacao.modelos.to.DestinatarioTO;
import net.sgq.transparencia.comunicacao.servicos.ComunicacaoService;

@Service
public class ComunicacaoServiceImpl implements ComunicacaoService {

	private Logger logger = LoggerFactory.getLogger(ComunicacaoServiceImpl.class);

	@Autowired
	private EnvioRepository envioRepo;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(cacheNames = "comunicadosEnviados")
	public void pushDeEventos(List<DestinatarioTO> destinatarios, List<? extends Comunicado> comunicados) {

		for (DestinatarioTO destinatario : destinatarios) {
			for (Comunicado comunicado : comunicados) {
				
				if(!envioRepo.existsByComunicadoIdAndComunicadoTipoAndDestinatarioId(comunicado.getId(), comunicado.getType(), destinatario.getId())) {
					logger.info("Push enviado para '{}': {}", destinatario.getDescricao(), comunicado);

					envioRepo.save(new Envio(comunicado, destinatario));
				} else {
					logger.debug("Comunicado {} já enviado para {}", comunicado.getId(), destinatario.getDescricao());
				}
			}
		}

	}

}
