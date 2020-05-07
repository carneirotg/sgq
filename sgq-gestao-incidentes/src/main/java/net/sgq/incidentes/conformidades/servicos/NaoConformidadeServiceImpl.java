package net.sgq.incidentes.conformidades.servicos;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
	public Page<NaoConformidade> listaNCs(Pageable pageable) {
		Page<NaoConformidade> ncs = this.repository.findAll(pageable);
		
		if(ncs == null) {
			ncs = new PageImpl<>(new ArrayList<>());
		}
		
		return ncs;
	}

	@Override
	public Page<NaoConformidade> listaNCs(String titulo, Pageable pageable) {
		Page<NaoConformidade> ncs = this.repository.findByTituloContaining(titulo, pageable);
		
		if(ncs == null) {
			ncs = new PageImpl<>(new ArrayList<>());
		}
		
		return ncs;
	}

	@Override
	public NaoConformidade consultaNC(Long id) {

		Optional<NaoConformidade> oNC = this.repository.findById(id);

		if (oNC.isEmpty()) {
			return null;
		}

		return oNC.get();
	}

	@Override
	public NaoConformidade consultaEntidadeNC(Long nCId) {
		Optional<NaoConformidade> oNC = this.repository.findById(nCId);

		if (oNC.isEmpty()) {
			return null;
		}

		return oNC.get();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Long salvarNC(NaoConformidade naoConformidade, Long id) {

		NaoConformidade nc = null;

		if (id == null || id == 0) {
			nc = novaNaoConformidade(naoConformidade);
		} else {
			nc = atualizaNaoConformidade(naoConformidade, id);
		}

		return nc.getId();
	}

	@Override
	public Page<NaoConformidade> listaNCs(Estado estado, Pageable pageable) {

		Page<NaoConformidade> resultado = null;

		if (estado == Estado.NAO_CONCLUIDA) {
			resultado = this.repository.findByEstadoNot(Estado.CONCLUIDA, pageable);
		} else {
			resultado = this.repository.findByEstado(estado, pageable);
		}
		
		if(resultado == null) {
			resultado = new PageImpl<>(new ArrayList<>());
		}

		return resultado;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void naoConformidadeMudaEstado(Long id, Estado estado) {
		Optional<NaoConformidade> oNC = this.repository.findById(id);

		NaoConformidade nc = validator.validaNCRetornada(id, oNC);

		if (validator.trasicaoValida(nc, estado)) {
			logger.info("NC({}) transicionou de {} para {}", id, nc.getEstado(), estado);
			nc.setEstado(estado);
		} else {
			throw new IllegalStateException(
					String.format("Transição de uma NC de %s para %s não é permitida", nc.getEstado(), estado));
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void associaNCANorma(Long ncId, Long normaId) {

		Norma norma = this.normaService.consultaNorma(normaId);
		Optional<NaoConformidade> oNC = this.repository.findById(ncId);

		NaoConformidade nc = validator.validaNCRetornada(ncId, oNC);
		nc.setNormaNaoConformidade(norma);

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void atualizaChecklist(Long ncId, Map<String, Boolean> checklist) {

		Optional<NaoConformidade> oNC = this.repository.findById(ncId);

		NaoConformidade nc = validator.validaNCRetornada(ncId, oNC);

		if (nc.getNormaNaoConformidade().getNormaId() == null) {
			throw new IllegalStateException("NC não possui norma associada");
		}

		nc.getNormaNaoConformidade().setCheckList(checklist);
	}

	private NaoConformidade novaNaoConformidade(NaoConformidade naoConformidade) {

		Artefato artefato = this.artefatoService.buscaArtefatoPor(naoConformidade.getArtefato().getId());

		validator.validaArtefato(naoConformidade, artefato);

		naoConformidade.setArtefato(artefato);

		return this.repository.save(naoConformidade);

	}

	private NaoConformidade atualizaNaoConformidade(NaoConformidade ncAtualizada, Long ncID) {

		Optional<NaoConformidade> oNC = this.repository.findById(ncID);

		NaoConformidade dbNC = validator.validaNCRetornada(ncID, oNC);

		if (dbNC.getEstado() == Estado.CONCLUIDA) {
			throw new IllegalStateException("NC já foi concluída e não pode mais ser alterada.");
		}

		return this.repository.save(ncAtualizada);
	}

}
