package net.sgq.incidentes.conformidades.servicos;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import net.sgq.incidentes.conformidades.modelos.NaoConformidade;
import net.sgq.incidentes.conformidades.modelos.enums.Estado;

public interface NaoConformidadeService {

	Page<NaoConformidade> listaNCs(Pageable pageable);

	Page<NaoConformidade>listaNCs(String nome, Pageable pageable);

	NaoConformidade consultaNC(Long id);
	
	Long salvarNC(NaoConformidade naoConformidadeTo, Long l);

	Page<NaoConformidade> listaNCs(Estado aberta, String titulo, Pageable pageable);

	void naoConformidadeMudaEstado(Long id, Estado estado);

	void associaNCANorma(Long ncId, Long normaId, OAuth2Authentication usuario);

	void atualizaChecklist(Long ncId, Map<String, Boolean> checklist);

	Map<String, Long> estatisticas();

}
