package net.sgq.transparencia.comunicacao.servicos;

import java.util.List;

import net.sgq.transparencia.comunicacao.modelos.to.DestinatarioTO;

public interface DestinatarioService {

	List<DestinatarioTO> todos();

	Long salvarDestinatario(DestinatarioTO destinatarioTO, Long id);
	void removeDestinatario(Long id);
	
}
