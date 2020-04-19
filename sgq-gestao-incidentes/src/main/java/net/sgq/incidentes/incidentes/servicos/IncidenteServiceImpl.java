package net.sgq.incidentes.incidentes.servicos;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import net.sgq.incidentes.conformidades.modelos.enums.Estado;
import net.sgq.incidentes.incidentes.modelos.Incidente;
import net.sgq.incidentes.incidentes.modelos.IncidenteRepository;
import net.sgq.incidentes.incidentes.modelos.to.IncidenteIdTO;
import net.sgq.incidentes.incidentes.modelos.to.IncidenteTO;
import net.sgq.incidentes.utils.EntityNotFoundException;

@Service
public class IncidenteServiceImpl implements IncidenteService {

	@Autowired
	private IncidenteRepository repository;

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

	private Incidente atualizaIncidente(IncidenteTO incidenteTo, Long id) {
		
		Optional<Incidente> oIC = this.repository.findById(id);

		if (oIC.isEmpty()) {
			throw new EntityNotFoundException("NaoConformidade", id);
		}

		Incidente incidente = oIC.get();

		if (incidente.getSituacao() == Estado.CONCLUIDA) {
			throw new IllegalArgumentException("Incidente já foi concluído e não pode mais ser alterada.");
		}

		incidente.fromTO(incidenteTo);

		return incidente;
	}

	private Incidente novoIncidente(IncidenteTO incidenteTo) {

		Incidente ic = new Incidente().fromTO(incidenteTo);

		return this.repository.save(ic);

	}

}
