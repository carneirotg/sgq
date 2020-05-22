package net.sgq.incidentes.artefatos.servicos;

import java.util.Map;

import org.springframework.data.domain.Page;

import net.sgq.incidentes.artefatos.modelos.Artefato;

public interface ArtefatoService {

	Artefato buscaArtefatoPor(Long id);
	Page<Artefato> buscaArtefatos(String nome, Integer pagina, Integer registros);

	Long salvaArtefato(Artefato artefato, Long id);
	void depreciaArtefato(Long id);
	Map<String, Long> estatisticas();

}
