package net.sgq.incidentes.incidentes.servicos;

import static org.assertj.core.api.Assertions.assertThat;
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

import net.sgq.incidentes.conformidades.modelos.NaoConformidade;
import net.sgq.incidentes.conformidades.modelos.enums.Estado;
import net.sgq.incidentes.conformidades.servicos.NaoConformidadeService;
import net.sgq.incidentes.incidentes.modelos.Incidente;
import net.sgq.incidentes.incidentes.modelos.IncidenteRepository;
import net.sgq.incidentes.incidentes.modelos.to.IncidenteTO;

@SpringBootTest
public class IncidenteServiceTests {

	@Autowired
	private IncidenteService service;
	
	@MockBean
	private IncidenteRepository repository;

	@MockBean
	private NaoConformidadeService ncService;

	@MockBean
	private IncidenteValidator validator;
	
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
		assertThat(service.listaIncidentes()).isNotNull().isNotEmpty();
	}
	
	@Test
	public void listaIncidentesPorTitulo() {
		preencheRepositorio();
		assertThat(service.listaIncidentes("Incidente x")).isNotNull().isNotEmpty();
	}
	
	@Test
	public void listaIncidentesEstadoJanela() {
		preencheRepositorio();
		assertThat(service.listaIncidentes(Estado.ABERTA, 1)).isNotNull().isNotEmpty();
	}
	
	@Test
	public void listaIncidentesEstado() {
		preencheRepositorio();
		assertThat(service.listaIncidentes(Estado.ABERTA, null)).isNotNull().isNotEmpty();
	}
	
	@Test
	public void listaIncidentesEstadoNaoConcluido() {
		preencheRepositorio();
		assertThat(service.listaIncidentes(Estado.NAO_CONCLUIDA, null)).isNotNull().isNotEmpty();
	}
	
	@Test
	public void novoIncidente() {
		Incidente ic = new Incidente();
		ic.setId(1L);
		
		when(repository.save(any())).thenReturn(ic);
		
		assertThat(service.salvarIncidente(Mockito.mock(IncidenteTO.class), 0L)).isEqualTo(ic.getId());
	}
	
	@Test
	public void atualizaIncidente() {
		Incidente ic = new Incidente();
		ic.setId(1L);
		
		when(repository.findById(any())).thenReturn(Optional.of(ic));
		
		assertThat(service.salvarIncidente(Mockito.mock(IncidenteTO.class), 1L)).isEqualTo(1L);
	}
	
	@Test
	public void atualizaIncidenteConcluido() {
		Incidente ic = new Incidente();
		ic.setId(1L);
		ic.setSituacao(Estado.CONCLUIDA);
		
		when(repository.findById(any())).thenReturn(Optional.of(ic));
		
		assertThrows(IllegalStateException.class, () -> {
			service.salvarIncidente(Mockito.mock(IncidenteTO.class), 1L);
		});
	}
	
	@Test
	public void adicionaNaoConformidade() {
		preencheRepositorio();
		service.adicionaNaoConformidade(1L, 1L);
	}
	
	@Test
	public void naoAdicionaNaoConformidadeDuplicada() {
		preencheRepositorio();
		when(validator.validaDuplicidadeNC(any(), any())).thenReturn(Boolean.TRUE);
		service.adicionaNaoConformidade(1L, 1L);
	}
	
	@Test
	public void removeNCsDeIncidente() {
		preencheRepositorio();
		
		service.removeNaoConformidade(2L, 2L);
		service.removeTodasNaoConformidades(1L);
	}
	
	@Test
	public void mudancaDeEstados() {
		preencheRepositorioEstados();
		
		when(validator.trasicaoValida(any(), any())).thenReturn(Boolean.TRUE);
		
		service.incidenteMudaEstado(1L, Estado.EM_ANALISE);
		service.incidenteMudaEstado(2L, Estado.CONCLUIDA);
		
		when(validator.trasicaoValida(any(), any())).thenReturn(Boolean.FALSE);
		
		assertThrows(IllegalStateException.class, () -> {
			service.incidenteMudaEstado(1L, Estado.EM_ANALISE);	
		});
	}
	
	private void preencheRepositorio() {
		List<Incidente> incidentes = new ArrayList<>();
		incidentes.add(new Incidente());
		
		when(repository.findAll()).thenReturn(incidentes);
		when(repository.findById(any())).thenReturn(Optional.of(new Incidente()));
		when(repository.findBySituacaoNot(eq(Estado.CONCLUIDA))).thenReturn(incidentes);
		when(repository.findBySituacao(any())).thenReturn(incidentes);
		when(repository.findByTituloContaining(Mockito.anyString())).thenReturn(incidentes);
		when(repository.findBySituacaoAndConcluidoEmAfter(any(), any())).thenReturn(incidentes);
		
		Incidente ic = new Incidente();
		NaoConformidade nc = new NaoConformidade();
		
		nc.setId(2L);
		ic.getNcEnvolvidas().add(nc);
		when(repository.findById(2L)).thenReturn(Optional.of(ic));
		
		when(ncService.consultaEntidadeNC(any())).thenReturn(new NaoConformidade());
		
	}
	
	private void preencheRepositorioEstados() {
		Incidente ic1 = new Incidente();
		ic1.setSituacao(Estado.ABERTA);
		when(repository.findById(1L)).thenReturn(Optional.of(ic1));
		
		Incidente ic2 = new Incidente();
		ic2.setSituacao(Estado.ABERTA);
		when(repository.findById(2L)).thenReturn(Optional.of(ic2));
	}
	
}
