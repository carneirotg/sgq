package net.sgq.incidentes.incidentes.servicos;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import net.sgq.incidentes.conformidades.modelos.NaoConformidade;
import net.sgq.incidentes.conformidades.modelos.enums.Estado;
import net.sgq.incidentes.incidentes.modelos.Incidente;

@SpringBootTest
public class IncidentesValidatorTests {

	@Autowired
	private IncidenteValidator validator;
	
	@Test
	public void validatorOk() {
		assertThat(validator).isNotNull();
	}
	
	@Test
	public void naoConformidadeValida() {
		NaoConformidade nc = new NaoConformidade();
		nc.setEstado(Estado.CONCLUIDA);
		
		assertDoesNotThrow(() -> validator.validaNC(nc, 1L));
	}
	
	@Test
	public void naoConformidadeNula() {
		assertThrows(EntityNotFoundException.class, () -> {
			validator.validaNC(null, 1L);
		});
	}
	
	@Test
	public void naoConformidadeNaoConcluida() {
		NaoConformidade nc = new NaoConformidade();
		nc.setEstado(Estado.ABERTA);
		
		assertThrows(IllegalStateException.class, () -> {
			validator.validaNC(nc, 1L);
		});
	}
	
	@Test
	public void transicaoValida() {
		Incidente incidente = new Incidente();
		incidente.setSituacao(Estado.ABERTA);
		incidente.setConclusao("Incidente investigado e conclusao fornecida");
		
		assertThat(validator.trasicaoValida(incidente, Estado.EM_ANALISE)).isTrue();
	}
	
	@Test
	public void transicaoIncidenteConcluido() {
		Incidente incidente = new Incidente();
		incidente.setSituacao(Estado.ABERTA);
		incidente.setConclusao("Incidente investigado e conclusao fornecida");
		
		assertThat(validator.trasicaoValida(incidente, Estado.CONCLUIDA)).isTrue();
	}
	
	@Test
	public void transicaoIncidenteNaoConcluido() {
		Incidente incidente = new Incidente();
		incidente.setSituacao(Estado.ABERTA);
		
		assertThrows(IllegalStateException.class, () -> {
			validator.trasicaoValida(incidente, Estado.CONCLUIDA);
		});
		
	}
	
	@Test
	public void transicaoIncidenteMesmoEstado() {
		Incidente incidente = new Incidente();
		incidente.setSituacao(Estado.ABERTA);
		
		assertThat(validator.trasicaoValida(incidente, Estado.ABERTA)).isFalse();
	}
	
	@Test
	public void transicaoIncidenteJaConcluido() {
		Incidente incidente = new Incidente();
		incidente.setSituacao(Estado.CONCLUIDA);
		
		assertThat(validator.trasicaoValida(incidente, Estado.CONCLUIDA)).isFalse();
		
	}
	
	@Test
	public void validaIncidenteExistente() {
		Optional<Incidente> oIC = Optional.ofNullable(null);
		
		assertThrows(EntityNotFoundException.class, () -> {
			validator.validaIncidenteRetornado(1L, oIC);
		});
	}
	
	@Test
	public void validaDuplicidadesDeNCs() {
		Incidente ic = new Incidente();
		NaoConformidade nc = new NaoConformidade();
		nc.setId(1L);
		
		ic.getNcEnvolvidas().add(nc);
		
		assertThat(validator.validaDuplicidadeNC(ic, 1L)).isTrue();
	}
	
}
