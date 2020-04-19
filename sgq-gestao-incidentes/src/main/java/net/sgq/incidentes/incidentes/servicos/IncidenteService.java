package net.sgq.incidentes.incidentes.servicos;

import java.util.List;

import net.sgq.incidentes.conformidades.modelos.enums.Estado;
import net.sgq.incidentes.incidentes.modelos.to.IncidenteIdTO;
import net.sgq.incidentes.incidentes.modelos.to.IncidenteTO;

public interface IncidenteService {

	IncidenteIdTO consultaIncidente(Long id);

	List<IncidenteIdTO> listaIncidentes();

	List<IncidenteIdTO> listaIncidentes(String nome);

	List<IncidenteIdTO> listaIncidentes(Estado estado);

	Long salvarIncidente(IncidenteTO incidente, Long id);
	
	void adicionaNaoConformidade(Long iId, Long nCId);
	void removeNaoConformidade(Long iId, Long nCId);
	void removeTodasNaoConformidades(Long id);

	void incidenteMudaEstado(Long iId, Estado aberta);

}
