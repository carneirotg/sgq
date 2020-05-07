package net.sgq.incidentes.conformidades.servicos;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Component;

import net.sgq.incidentes.artefatos.modelos.Artefato;
import net.sgq.incidentes.conformidades.modelos.NaoConformidade;
import net.sgq.incidentes.conformidades.modelos.enums.Estado;

@Component
public class NaoConformidadeValidator {

	public NaoConformidade validaNCRetornada(Long ncId, Optional<NaoConformidade> oNC) {
		if (oNC.isEmpty()) {
			throw new EntityNotFoundException(String.format("Entidade do tipo 'NaoConformidade' não encontrada para Id %d", ncId));
		}
		
		return oNC.get();
	}

	public boolean trasicaoValida(NaoConformidade nc, Estado estado) {
		return !(nc.getEstado() == estado || nc.getEstado() == Estado.CONCLUIDA);
	}

	public void validaArtefato(NaoConformidade naoConformidade, Artefato artefato) {
		if (artefato == null) {
			throw new EntityNotFoundException(String.format("Entidade do tipo 'Artefato' não encontrada para Id %d", naoConformidade.getArtefato().getId()));
		}

		if (artefato.getDepreciado()) {
			throw new IllegalStateException("Não conformidade não pode ser criada com artefato depreciado");
		}
	}

}