package net.sgq.incidentes.conformidades.servicos;

import java.util.List;

import net.sgq.incidentes.conformidades.modelos.enums.Estado;
import net.sgq.incidentes.conformidades.modelos.to.NaoConformidadeIdTO;
import net.sgq.incidentes.conformidades.modelos.to.NaoConformidadeTO;

public interface NaoConformidadeService {

	List<NaoConformidadeIdTO> listaNCs();

	List<NaoConformidadeIdTO>listaNCs(String nome);

	NaoConformidadeIdTO consultaNC(Long id);

	Long salvarNC(NaoConformidadeTO naoConformidadeTo, Long l);

	List<NaoConformidadeIdTO> listaNCs(Estado aberta);

	void naoConformidadeMudaEstado(Long id, Estado estado);

	void associaNCANorma(Long ncId, Long normaId);

}
