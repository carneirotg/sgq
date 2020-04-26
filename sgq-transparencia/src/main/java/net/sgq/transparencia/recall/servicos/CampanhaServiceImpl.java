package net.sgq.transparencia.recall.servicos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import net.sgq.transparencia.recall.modelos.CampanhaRecall;
import net.sgq.transparencia.recall.modelos.CampanhaRecallRepository;
import net.sgq.transparencia.recall.modelos.NCRepository;
import net.sgq.transparencia.recall.modelos.NaoConformidade;
import net.sgq.transparencia.recall.modelos.enums.Estado;
import net.sgq.transparencia.recall.modelos.to.CampanhaRecallTO;

@Service
public class CampanhaServiceImpl implements CampanhaService {

	@Autowired
	private CampanhaRecallRepository repository;

	@Autowired
	private NCRepository ncRepository;

	private Logger logger = LoggerFactory.getLogger(CampanhaServiceImpl.class);

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Long salvar(CampanhaRecallTO campanha) {

		trataNCSCampanha(campanha);
		CampanhaRecall campanhaRecall = this.repository.save(new CampanhaRecall().fromTO(campanha));

		return campanhaRecall.getId();
	}

	private void trataNCSCampanha(CampanhaRecallTO campanha) {
		
		List<NaoConformidade> ncs = new ArrayList<>();
		
		for(NaoConformidade nc : campanha.getNcsEnvolvidas()) {
			
			Optional<NaoConformidade> oNC = this.ncRepository.findById(nc.getId());
			
			if(oNC.isPresent()) {
				ncs.add(oNC.get());
			} else {
				ncs.add(this.ncRepository.save(nc));
			}
			
		}
		
		campanha.setNcsEnvolvidas(ncs);
		
	}

	@Override
	public List<CampanhaRecallTO> buscar(Estado estado) {

		List<CampanhaRecall> campanhas;

		if (estado == null) {
			campanhas = this.repository.findAll();
		} else {
			campanhas = this.repository.findByEstadoCampanha(estado);
		}

		return campanhas.stream().map(c -> c.toTO()).collect(Collectors.toList());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void atualizaDataTermino(Long id, Date novaDataTermino) {
		Optional<CampanhaRecall> oCampanha = buscaCampanhaValida(id);

		CampanhaRecall campanha = oCampanha.get();
		campanha.setFim(novaDataTermino);

		logger.info("Data de término da campanha {} foi atualizado para {}", id, novaDataTermino);

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void concluiCampanha(Long id) {
		Optional<CampanhaRecall> oCampanha = buscaCampanhaValida(id);

		CampanhaRecall campanha = oCampanha.get();

		validaEstadoCampanha(campanha);
		campanha.setEstadoCampanha(Estado.CONCLUIDA);

		if (campanha.getFim().after(new Date())) {
			logger.warn("Campanha {} foi concluída antes do período", id);
		} else {
			logger.info("Campanha {} concluída", id);
		}

	}

	private void validaEstadoCampanha(CampanhaRecall campanha) {
		if (Estado.CONCLUIDA == campanha.getEstadoCampanha()) {
			logger.warn("Campanha já concluída teve atualização de dados negada");
			new IllegalStateException("Campanha já foi concluída, não pode ter seu estado alterado");
		}
	}

	private Optional<CampanhaRecall> buscaCampanhaValida(Long id) {
		Optional<CampanhaRecall> oCampanha = this.repository.findById(id);

		if (oCampanha.isEmpty()) {
			throw new EntityNotFoundException(String.format("Entidade 'Campanha' não encontrada com id %d", id));
		}

		return oCampanha;
	}

}
