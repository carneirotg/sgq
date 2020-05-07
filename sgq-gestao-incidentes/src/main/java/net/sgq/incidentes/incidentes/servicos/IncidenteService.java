package net.sgq.incidentes.incidentes.servicos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import net.sgq.incidentes.conformidades.modelos.enums.Estado;
import net.sgq.incidentes.incidentes.modelos.Incidente;

public interface IncidenteService {

	Incidente consultaIncidente(Long id);

	Page<Incidente> listaIncidentes(Pageable pageable);

	Page<Incidente> listaIncidentes(String nome, Pageable pageable);

	Page<Incidente> listaIncidentes(Estado estado, Integer janelaMinutos, Pageable pageable);

	Long salvarIncidente(Incidente incidente, Long id);
	
	void adicionaNaoConformidade(Long iId, Long nCId);
	void removeNaoConformidade(Long iId, Long nCId);
	void removeTodasNaoConformidades(Long id);

	void incidenteMudaEstado(Long iId, Estado aberta);

}
