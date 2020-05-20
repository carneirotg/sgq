package net.sgq.transparencia.recall.servicos;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import net.sgq.transparencia.recall.modelos.enums.Estado;
import net.sgq.transparencia.recall.modelos.to.CampanhaRecallTO;

public interface CampanhaService {

	CampanhaRecallTO consultaId(Long id);
	Long salvar(CampanhaRecallTO campanha);
	
	Page<CampanhaRecallTO> buscar(Estado estado, String termo, Pageable pageRequest);
	List<CampanhaRecallTO> buscar(Estado estado, Integer janelaMinutos);
	
	void atualizaCampanha(Long id, CampanhaRecallTO campanha);
	void atualizaDataTermino(Long id, Date novaDataTermino);
	void concluiCampanha(Long id);
	
}
