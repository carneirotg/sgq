package net.sgq.incidentes.incidentes.servicos;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import net.sgq.incidentes.conformidades.modelos.NaoConformidade;
import net.sgq.incidentes.conformidades.modelos.enums.Estado;
import net.sgq.incidentes.conformidades.servicos.NaoConformidadeService;
import net.sgq.incidentes.incidentes.modelos.Incidente;
import net.sgq.incidentes.incidentes.modelos.IncidenteRepository;

@SpringBootTest
public class IncidenteServiceTests {

	@Autowired
	private IncidenteService service;
	
	@MockBean
	private IncidenteRepository repository;

	@MockBean
	private NaoConformidadeService ncService;

	@Test
	public void serviceOk() {
		assertThat(service).isNotNull();
	}
	
	@Test
	public void consultasIncidentes() {
		when(repository.findById(Mockito.eq(1L))).thenReturn(Optional.of(new Incidente()));
		when(repository.findById(Mockito.eq(2L))).thenReturn(Optional.ofNullable(null));
		
		assertThat(service.consultaIncidente(1L)).isNotNull();
		assertThat(service.consultaIncidente(2L)).isNull();
	}
	
	@Test
	public void listaIncidentes() {
		preencheRepositorio();
		assertThat(service.listaIncidentes(Pageable.unpaged()).getContent()).isNotNull().isNotEmpty();
	}
	
	@Test
	public void listaIncidentesPorTitulo() {
		preencheRepositorio();
		assertThat(service.listaIncidentes("Incidente x", Pageable.unpaged())).isNotNull().isNotEmpty();
	}
	
	@Test
	public void listaIncidentesEstadoJanela() {
		preencheRepositorio();
		assertThat(service.listaIncidentes(Estado.ABERTA, 1, Pageable.unpaged())).isNotNull().isNotEmpty();
	}
	
	@Test
	public void listaIncidentesEstado() {
		preencheRepositorio();
		assertThat(service.listaIncidentes(Estado.ABERTA, null, Pageable.unpaged())).isNotNull().isNotEmpty();
	}
	
	@Test
	public void listaIncidentesEstadoNaoConcluido() {
		preencheRepositorio();
		assertThat(service.listaIncidentes(Estado.NAO_CONCLUIDA, null, Pageable.unpaged())).isNotNull().isNotEmpty();
	}
	
	@Test
	public void novoIncidente() {
		Incidente ic = new Incidente();
		ic.setId(1L);
		
		when(repository.save(any())).thenReturn(ic);
		
		assertThat(service.salvarIncidente(Mockito.mock(Incidente.class), 0L)).isEqualTo(ic.getId());
	}
	
	@Test
	public void atualizaIncidenteConcluido() {
		Incidente ic = new Incidente();
		ic.setId(1L);
		ic.setSituacao(Estado.CONCLUIDA);
		
		when(repository.findById(any())).thenReturn(Optional.of(ic));
		
		assertThrows(IllegalStateException.class, () -> {
			service.salvarIncidente(Mockito.mock(Incidente.class), 1L);
		});
	}
	
	@Test
	public void adicionaNaoConformidade() {
		preencheRepositorio();
		assertDoesNotThrow(() -> service.adicionaNaoConformidade(1L, 1L));
	}
	
	@Test
	public void naoAdicionaNaoConformidadeDuplicada() {
		preencheRepositorio();
		assertDoesNotThrow(() -> service.adicionaNaoConformidade(1L, 1L));
	}
	
	@Test
	public void removeNCsDeIncidente() {
		preencheRepositorio();
		
		assertDoesNotThrow(() -> service.removeNaoConformidade(2L, 2L));
		assertDoesNotThrow(() -> service.removeTodasNaoConformidades(1L));
	}
	
	@Test
	public void mudancaDeEstados() {
		preencheRepositorioEstados();
		
		
		assertDoesNotThrow(() -> service.incidenteMudaEstado(1L, Estado.EM_ANALISE));
		assertDoesNotThrow(() -> service.incidenteMudaEstado(2L, Estado.CONCLUIDA));
		
		assertThrows(IllegalStateException.class, () -> {
			service.incidenteMudaEstado(1L, Estado.EM_ANALISE);	
		});
	}
	
	private void preencheRepositorio() {
		List<Incidente> incidentes = new ArrayList<>();
		incidentes.add(new Incidente());
		
		Page<Incidente> pgIC = new PageImpl<>(incidentes);
		
		when(repository.findAll()).thenReturn(incidentes);
		when(repository.findAll(any(Pageable.class))).thenReturn(pgIC);
		when(repository.findById(any())).thenReturn(Optional.of(new Incidente()));
		when(repository.findBySituacaoNot(eq(Estado.CONCLUIDA), any())).thenReturn(pgIC);
		when(repository.findBySituacao(any(), any())).thenReturn(pgIC);
		when(repository.findByTituloContaining(Mockito.anyString(), any())).thenReturn(pgIC);
		when(repository.findBySituacaoAndConcluidoEmAfter(any(), any(), any())).thenReturn(pgIC);
		
		Incidente ic = new Incidente();
		NaoConformidade nc1 = new NaoConformidade();
		NaoConformidade nc2 = new NaoConformidade();
		
		nc1.setId(2L);
		ic.getNcEnvolvidas().add(nc1);
		when(repository.findById(2L)).thenReturn(Optional.of(ic));
		
		nc2.setEstado(Estado.CONCLUIDA);
		when(ncService.consultaEntidadeNC(any())).thenReturn(nc2);
		
	}
	
	private void preencheRepositorioEstados() {
		Incidente ic1 = new Incidente();
		ic1.setSituacao(Estado.ABERTA);
		when(repository.findById(1L)).thenReturn(Optional.of(ic1));
		
		Incidente ic2 = new Incidente();
		ic2.setSituacao(Estado.ABERTA);
		ic2.setConclusao("Incidente concluido");
		when(repository.findById(2L)).thenReturn(Optional.of(ic2));
	}
	
}
