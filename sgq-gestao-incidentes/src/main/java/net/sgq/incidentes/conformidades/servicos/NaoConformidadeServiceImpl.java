package net.sgq.incidentes.conformidades.servicos;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import net.sgq.incidentes.artefatos.modelos.Artefato;
import net.sgq.incidentes.artefatos.servicos.ArtefatoService;
import net.sgq.incidentes.clientes.GestaoNormasClient;
import net.sgq.incidentes.conformidades.modelos.NaoConformidade;
import net.sgq.incidentes.conformidades.modelos.NaoConformidadeRepository;
import net.sgq.incidentes.conformidades.modelos.Norma;
import net.sgq.incidentes.conformidades.modelos.enums.Estado;
import net.sgq.incidentes.conformidades.modelos.to.NaoConformidadeIdTO;
import net.sgq.incidentes.conformidades.modelos.to.NaoConformidadeTO;
import net.sgq.incidentes.utils.EntityNotFoundException;

@Service
public class NaoConformidadeServiceImpl implements NaoConformidadeService {

	@Autowired
	private NaoConformidadeRepository repository;
	
	@Autowired
	private ArtefatoService artefatoService;
	
	@Autowired
	private GestaoNormasClient normaService;
	
	@Autowired
	private NaoConformidadeValidator validator;

	private Logger logger = LoggerFactory.getLogger(NaoConformidadeServiceImpl.class);
	
	@Override
	public List<NaoConformidadeIdTO> listaNCs() {
		return this.repository.findAll().stream().map(nc -> nc.toTOId()).collect(Collectors.toList());
	}

	@Override
	public List<NaoConformidadeIdTO> listaNCs(String titulo) {
		return this.repository.findByTituloContaining(titulo).stream().map(nc -> nc.toTOId()).collect(Collectors.toList());
	}

	@Override
	public NaoConformidadeIdTO consultaNC(Long id) {
		
		Optional<NaoConformidade> oNC = this.repository.findById(id);
		
		if(oNC.isEmpty()) {
			return null;
		}
		
		return oNC.get().toTOId();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Long salvarNC(NaoConformidadeTO naoConformidadeTo, Long id) {

		NaoConformidade nc = null; 
		
		if(id == null || id == 0) {
			nc = novaNaoConformidade(naoConformidadeTo);
		} else {
			nc = atualizaNaoConformidade(naoConformidadeTo, id);
		}
		
		return nc.getId();
	}

	@Override
	public List<NaoConformidadeIdTO> listaNCs(Estado estado) {
		return this.repository.findByEstado(estado).stream().map(nc -> nc.toTOId()).collect(Collectors.toList());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void naoConformidadeMudaEstado(Long id, Estado estado) {
		Optional<NaoConformidade> oNC = this.repository.findById(id);
		
		validator.validaNCRetornada(id, oNC);
		
		NaoConformidade nc = oNC.get();
		
		if(validator.trasicaoValida(nc, estado)) {
			logger.info("NC({}) transicionou de {} para {}", id, nc.getEstado(), estado);
			nc.setEstado(estado);
		} else {
			throw new IllegalStateException(String.format("Transica de uma NC de %s para %s não é pemitida", nc.getEstado(), estado));
		}
		
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void associaNCANorma(Long ncId, Long normaId) {

		Norma norma = this.normaService.consultaNorma(normaId);
		Optional<NaoConformidade> oNC = this.repository.findById(ncId);
		
		validator.validaNCRetornada(ncId, oNC);
		
		NaoConformidade nc = oNC.get();
		nc.setNormaNaoConformidade(norma);
		
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void atualizaChecklist(Long ncId, Map<String, Boolean> checklist) {
		
		Optional<NaoConformidade> oNC = this.repository.findById(ncId);
		
		validator.validaNCRetornada(ncId, oNC);
		
		NaoConformidade nc = oNC.get();
		
		if(nc.getNormaNaoConformidade().getNormaId() == null) {
			throw new IllegalStateException("NC não possui norma associada");
		}
		
		nc.getNormaNaoConformidade().setCheckList(checklist);
	}

	private NaoConformidade novaNaoConformidade(NaoConformidadeTO naoConformidadeTo) {
		
		Artefato artefato = this.artefatoService.buscaEntidadeArtefatoPor(naoConformidadeTo.getArtefato());
		
		validator.validaArtefato(naoConformidadeTo, artefato);
		
		NaoConformidade nc = new NaoConformidade().fromTO(naoConformidadeTo);
		nc.setArtefato(artefato);
		
		return this.repository.save(nc);
		
	}
	
	private NaoConformidade atualizaNaoConformidade(NaoConformidadeTO naoConformidadeTo, Long id) {
		Optional<NaoConformidade> oNC = this.repository.findById(id);
		
		if(oNC.isEmpty()) {
			throw new EntityNotFoundException("NaoConformidade", id);
		}
		
		NaoConformidade nc = oNC.get();
		
		if(nc.getEstado() == Estado.CONCLUIDA) {
			throw new IllegalArgumentException("NC já foi concluída e não pode mais ser alterada.");
		}
		
		nc.fromTO(naoConformidadeTo);
		
		return nc;
	}

}
