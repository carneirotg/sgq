package net.sgq.incidentes.conformidades.servicos;

import java.util.List;
import java.util.Map;

import net.sgq.incidentes.conformidades.modelos.NaoConformidade;
import net.sgq.incidentes.conformidades.modelos.enums.Estado;
import net.sgq.incidentes.conformidades.modelos.to.NaoConformidadeIdTO;
import net.sgq.incidentes.conformidades.modelos.to.NaoConformidadeTO;

public interface NaoConformidadeService {

	List<NaoConformidadeIdTO> listaNCs();

	List<NaoConformidadeIdTO>listaNCs(String nome);

	NaoConformidadeIdTO consultaNC(Long id);
	
	NaoConformidade consultaEntidadeNC(Long nCId);

	Long salvarNC(NaoConformidadeTO naoConformidadeTo, Long l);

	List<NaoConformidadeIdTO> listaNCs(Estado aberta);

	void naoConformidadeMudaEstado(Long id, Estado estado);

	void associaNCANorma(Long ncId, Long normaId);

	void atualizaChecklist(Long ncId, Map<String, Boolean> checklist);

}
