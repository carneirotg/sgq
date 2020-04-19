package net.sgq.incidentes.conformidades.servicos;

import java.util.Optional;

import org.springframework.stereotype.Component;

import net.sgq.incidentes.artefatos.modelos.Artefato;
import net.sgq.incidentes.conformidades.modelos.NaoConformidade;
import net.sgq.incidentes.conformidades.modelos.enums.Estado;
import net.sgq.incidentes.conformidades.modelos.to.NaoConformidadeTO;
import net.sgq.incidentes.utils.EntityNotFoundException;

@Component
public class NaoConformidadeValidator {

	public void validaNCRetornada(Long ncId, Optional<NaoConformidade> oNC) {
		if (oNC.isEmpty()) {
			throw new EntityNotFoundException("Artefato", ncId);
		}
	}

	public boolean trasicaoValida(NaoConformidade nc, Estado estado) {

		if (nc.getEstado() == estado || nc.getEstado() == Estado.CONCLUIDA) {
			return false;
		}

		return true;
	}

	public void validaArtefato(NaoConformidadeTO naoConformidadeTo, Artefato artefato) {
		if (artefato == null) {
			throw new EntityNotFoundException("Artefato", naoConformidadeTo.getArtefato());
		}

		if (artefato.getDepreciado()) {
			throw new IllegalStateException("Não conformidade não pode ser criada com artefato depreciado");
		}
	}

}