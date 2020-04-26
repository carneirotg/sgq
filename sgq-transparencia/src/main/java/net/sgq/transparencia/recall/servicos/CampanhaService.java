package net.sgq.transparencia.recall.servicos;

import java.util.Date;
import java.util.List;

import net.sgq.transparencia.recall.modelos.enums.Estado;
import net.sgq.transparencia.recall.modelos.to.CampanhaRecallTO;

public interface CampanhaService {

	Long salvar(CampanhaRecallTO campanha);
	
	List<CampanhaRecallTO> buscar(Estado estado);
	
	void atualizaDataTermino(Long id, Date novaDataTermino);
	void concluiCampanha(Long id);
	
}
