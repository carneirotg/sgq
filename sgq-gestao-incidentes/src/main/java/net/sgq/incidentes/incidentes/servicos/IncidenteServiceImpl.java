package net.sgq.incidentes.incidentes.servicos;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import net.sgq.incidentes.conformidades.modelos.NaoConformidade;
import net.sgq.incidentes.conformidades.modelos.enums.Estado;
import net.sgq.incidentes.conformidades.servicos.NaoConformidadeService;
import net.sgq.incidentes.incidentes.modelos.Incidente;
import net.sgq.incidentes.incidentes.modelos.IncidenteRepository;
import net.sgq.incidentes.incidentes.modelos.to.IncidenteIdTO;
import net.sgq.incidentes.incidentes.modelos.to.IncidenteTO;

@Service
public class IncidenteServiceImpl implements IncidenteService {

	@Autowired
	private IncidenteRepository repository;
	
	@Autowired
	private NaoConformidadeService ncService;
	
	@Autowired
	private IncidenteValidator validator;
	
	private Logger logger = LoggerFactory.getLogger(IncidenteServiceImpl.class);

	@Override
	public IncidenteIdTO consultaIncidente(Long id) {

		Optional<Incidente> oIC = this.repository.findById(id);

		if (oIC.isEmpty()) {
			return null;
		}

		return oIC.get().toTOId();

	}

	@Override
	public List<IncidenteIdTO> listaIncidentes() {
		return this.repository.findAll().stream().map(i -> i.toTOId()).collect(Collectors.toList());
	}

	@Override
	public List<IncidenteIdTO> listaIncidentes(String nome) {
		return this.repository.findByTituloContaining(nome).stream().map(i -> i.toTOId()).collect(Collectors.toList());
	}

	@Override
	public List<IncidenteIdTO> listaIncidentes(Estado estado) {
		return this.repository.findBySituacao(estado).stream().map(i -> i.toTOId()).collect(Collectors.toList());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Long salvarIncidente(IncidenteTO incidente, Long id) {
		Incidente ic = null;

		if (id == null || id == 0) {
			ic = novoIncidente(incidente);
		} else {
			ic = atualizaIncidente(incidente, id);
		}

		return ic.getId();
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void adicionaNaoConformidade(Long iId, Long nCId) {
		NaoConformidade nc = this.ncService.consultaEntidadeNC(nCId);
		
		validator.validaNC(nc, nCId);
		
		Optional<Incidente> oIc = this.repository.findById(iId);
		validator.validaIncidenteRetornado(iId, oIc);
		
		Incidente incidente = oIc.get();
		
		if(!validator.validaDuplicidadeNC(incidente, nCId)) {
			incidente.getNcEnvolvidas().add(nc);
		}
		
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void removeNaoConformidade(Long iId, Long nCId) {
		
		Optional<Incidente> oIc = this.repository.findById(nCId);
		validator.validaIncidenteRetornado(iId, oIc);
		
		Incidente incidente = oIc.get();
		
		if(incidente.getNcEnvolvidas().removeIf(nc -> nc.getId() == nCId)) {
			logger.info("NC #{} removida de Incidente #{}", nCId, iId);
		}
		
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void removeTodasNaoConformidades(Long id) {
		Incidente incidente = retornaIncidenteValidado(id);
		
		incidente.getNcEnvolvidas().clear();
		logger.info("Removida todas as NCs incluidas no incidente {}", id);
		
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void incidenteMudaEstado(Long iId, Estado estado) {

		Optional<Incidente> oIc = this.repository.findById(iId);
		
		validator.validaIncidenteRetornado(iId, oIc);
		
		Incidente nc = oIc.get();
		
		if(validator.trasicaoValida(nc, estado)) {
			logger.info("NC({}) transicionou de {} para {}", iId, nc.getSituacao(), estado);
			nc.setSituacao(estado);
			
			if(estado == Estado.CONCLUIDA) {
				nc.setConcluidoEm(new Date());
			}
			
		} else {
			throw new IllegalStateException(String.format("Transica de um Incidente de %s para %s não é permitida", nc.getSituacao(), estado));
		}
		
	}

	private Incidente retornaIncidenteValidado(Long id) {
		Optional<Incidente> oIc = this.repository.findById(id);
		validator.validaIncidenteRetornado(id, oIc);
		
		return oIc.get();
	}
	
	private Incidente atualizaIncidente(IncidenteTO incidenteTo, Long id) {
		
		Optional<Incidente> oIC = this.repository.findById(id);

		validator.validaIncidenteRetornado(id, oIC);

		Incidente incidente = oIC.get();

		if (incidente.getSituacao() == Estado.CONCLUIDA) {
			throw new IllegalStateException("Incidente já foi concluído e não pode mais ser alterado.");
		}

		incidente.fromTO(incidenteTo);

		return incidente;
	}

	private Incidente novoIncidente(IncidenteTO incidenteTo) {

		Incidente ic = new Incidente().fromTO(incidenteTo);

		return this.repository.save(ic);

	}

}
