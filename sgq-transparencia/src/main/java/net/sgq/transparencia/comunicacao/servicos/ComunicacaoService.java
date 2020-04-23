package net.sgq.transparencia.comunicacao.servicos;

import java.util.List;

import net.sgq.transparencia.comunicacao.modelos.Comunicado;
import net.sgq.transparencia.comunicacao.modelos.to.DestinatarioTO;

public interface ComunicacaoService {

	void pushDeEventos(List<DestinatarioTO> destinatarios, List<? extends Comunicado> comunicados);

}
