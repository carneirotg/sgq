package net.sgq.incidentes.artefatos.servicos;

import java.util.List;

import net.sgq.incidentes.artefatos.modelos.Artefato;
import net.sgq.incidentes.artefatos.modelos.to.ArtefatoIdTO;
import net.sgq.incidentes.artefatos.modelos.to.ArtefatoTO;

public interface ArtefatoService {

	Artefato buscaEntidadeArtefatoPor(Long id);
	
	ArtefatoIdTO buscaArtefatoPor(Long id);
	List<ArtefatoIdTO> buscaArtefatos(String nome, Integer pagina, Integer registros);

	Long salvaArtefato(ArtefatoTO artefatoTO, Long id);
	void depreciaArtefato(Long id);

}
