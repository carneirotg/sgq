package net.sgq.incidentes.incidentes.servicos;

import java.util.Optional;

import org.springframework.stereotype.Component;

import net.sgq.incidentes.conformidades.modelos.NaoConformidade;
import net.sgq.incidentes.conformidades.modelos.enums.Estado;
import net.sgq.incidentes.incidentes.modelos.Incidente;
import net.sgq.incidentes.utils.EntityNotFoundException;

@Component
public class IncidenteValidator {

	public void validaNC(NaoConformidade nc, Long ncId) {
		if (nc == null) {
			throw new EntityNotFoundException("NaoConformidade", ncId);
		}

		if (nc.getEstado() != Estado.CONCLUIDA) {
			throw new IllegalStateException("Um incidente não pode ser associado a uma não conformidade não concluída");
		}
	}

	public Incidente validaIncidenteRetornado(Long iId, Optional<Incidente> oIc) {
		if (oIc.isEmpty()) {
			throw new EntityNotFoundException("Incidente", iId);
		}
		
		return oIc.get();
	}
	
	public Boolean validaDuplicidadeNC(Incidente ic, Long ncId) {
		return ic.getNcEnvolvidas().stream().anyMatch(nc -> nc.getId().equals(ncId));
	}
	
	public boolean trasicaoValida(Incidente ic, Estado estado) {

		if (ic.getSituacao() == estado || ic.getSituacao() == Estado.CONCLUIDA) {
			return false;
		} else if(estado == Estado.CONCLUIDA && ("".equalsIgnoreCase(ic.getConclusao()) || ic.getConclusao() == null)) {
			throw new IllegalStateException("Não é possível fechar um Incidente sem fornecer a conclusão do mesmo");
		}

		return true;
	}

}
