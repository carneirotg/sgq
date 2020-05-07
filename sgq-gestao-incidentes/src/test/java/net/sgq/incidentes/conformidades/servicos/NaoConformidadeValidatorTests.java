package net.sgq.incidentes.conformidades.servicos;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import net.sgq.incidentes.artefatos.modelos.Artefato;
import net.sgq.incidentes.conformidades.modelos.NaoConformidade;
import net.sgq.incidentes.conformidades.modelos.enums.Estado;
	
@SpringBootTest
public class NaoConformidadeValidatorTests {

	@Autowired
	private NaoConformidadeValidator validator;

	@Test
	public void validatorOk() {
		assertThat(validator).isNotNull();
	}

	@Test
	public void excecaoQuandoNCVazia() {
		assertThrows(EntityNotFoundException.class, () -> {
			validator.validaNCRetornada(1L, Optional.ofNullable(null));
		});
	}

	@Test
	public void transicaoValida() {
		NaoConformidade nc = new NaoConformidade();
		nc.setEstado(Estado.ABERTA);

		assertThat(validator.trasicaoValida(nc, Estado.EM_ANALISE)).isTrue();
	}

	@Test
	public void transicaoInvalida() {
		NaoConformidade nc = new NaoConformidade();
		nc.setEstado(Estado.ABERTA);

		assertThat(validator.trasicaoValida(nc, Estado.ABERTA)).isFalse();
	}

	@Test
	public void transicaoConcluidaInvalida() {
		NaoConformidade nc = new NaoConformidade();
		nc.setEstado(Estado.CONCLUIDA);

		assertThat(validator.trasicaoValida(nc, Estado.ABERTA)).isFalse();
	}

	@Test
	public void validaArtefato() {
		NaoConformidade nc = new NaoConformidade();
		Artefato artefato = new Artefato();
		artefato.setId(1L);
		artefato.setDepreciado(Boolean.FALSE);

		nc.setArtefato(artefato);

		assertDoesNotThrow(() -> validator.validaArtefato(nc, artefato));
	}

	@Test
	public void validaArtefatoNulo() {
		Artefato a = new Artefato();
		a.setId(1L);
		
		NaoConformidade nc = new NaoConformidade();
		nc.setArtefato(a);

		assertThrows(EntityNotFoundException.class, () -> {
			validator.validaArtefato(nc, null);
		});
	}
	
	@Test
	public void validaArtefatoDepreciado() {
		NaoConformidade nc = new NaoConformidade();
		Artefato artefato = new Artefato();
		artefato.setId(1L);
		artefato.setDepreciado(Boolean.TRUE);

		nc.setArtefato(artefato);

		assertThrows(IllegalStateException.class, () -> {
			validator.validaArtefato(nc, artefato);
		});
	}

}
