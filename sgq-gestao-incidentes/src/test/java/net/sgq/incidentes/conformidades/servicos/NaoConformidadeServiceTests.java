package net.sgq.incidentes.conformidades.servicos;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import net.sgq.incidentes.artefatos.modelos.Artefato;
import net.sgq.incidentes.artefatos.servicos.ArtefatoService;
import net.sgq.incidentes.clientes.GestaoNormasClient;
import net.sgq.incidentes.conformidades.modelos.NaoConformidade;
import net.sgq.incidentes.conformidades.modelos.NaoConformidadeRepository;
import net.sgq.incidentes.conformidades.modelos.Norma;
import net.sgq.incidentes.conformidades.modelos.enums.Estado;
import net.sgq.incidentes.conformidades.modelos.to.NaoConformidadeTO;
import net.sgq.incidentes.utils.EntityNotFoundException;

@SpringBootTest
public class NaoConformidadeServiceTests {

	@Autowired
	private NaoConformidadeService service;

	@MockBean
	private NaoConformidadeRepository repository;

	@MockBean
	private ArtefatoService artefatoService;

	@MockBean
	private GestaoNormasClient normaService;

	@MockBean
	private NaoConformidadeValidator validator;

	@Test
	public void serviceOk() {
		assertThat(service).isNotNull();
	}
	
	@Test
	public void listaNCs() {
		assertThat(service.listaNCs()).isNotNull();
	}
	
	@Test
	public void listaNCsPorTitulo() {
		assertThat(service.listaNCs("abc")).isNotNull();
	}
	
	@Test
	public void consultaNCsPorEstado() {
		List<NaoConformidade> ncs = new ArrayList<>();
		Artefato art = new Artefato();
		NaoConformidade nc = new NaoConformidade();
		
		art.setId(1L);
		nc.setArtefato(art);
		ncs.add(nc);
		
		when(repository.findByEstado(Mockito.any())).thenReturn(ncs);
		
		assertThat(service.listaNCs(Estado.ABERTA)).isNotNull().size().isEqualTo(1);
	}
	
	@Test
	public void consultaNCsNaoConcluidas() {
		List<NaoConformidade> ncs = new ArrayList<>();
		Artefato art = new Artefato();
		NaoConformidade nc = new NaoConformidade();
		
		art.setId(1L);
		nc.setArtefato(art);
		ncs.add(nc);
		
		when(repository.findByEstadoNot(Mockito.any())).thenReturn(ncs);
		
		assertThat(service.listaNCs(Estado.NAO_CONCLUIDA)).isNotNull().size().isEqualTo(1);
	}
	
	@Test
	public void consultaNCExistente() {
		Artefato art = new Artefato();
		NaoConformidade nc = new NaoConformidade();
		nc.setArtefato(art);
		
		when(repository.findById(anyLong())).thenReturn(Optional.of(nc));
		
		assertThat(service.consultaNC(1L)).isNotNull();
	}
	
	@Test
	public void consultaNCInexistente() {
		
		when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(null));
		
		assertThat(service.consultaNC(1L)).isNull();
	}
	
	@Test
	public void consultaEntidadeNCExistente() {
		Artefato art = new Artefato();
		NaoConformidade nc = new NaoConformidade();
		nc.setArtefato(art);
		
		when(repository.findById(anyLong())).thenReturn(Optional.of(nc));
		
		assertThat(service.consultaEntidadeNC(1L)).isNotNull();
	}
	
	@Test
	public void consultaEntidadeNCInexistente() {
		
		when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(null));
		
		assertThat(service.consultaEntidadeNC(1L)).isNull();
	}
	
	@Test
	public void transicaoDeEstado() {
		NaoConformidade nc = new NaoConformidade();
		nc.setId(1L);
		nc.setEstado(Estado.ABERTA);
		
		when(repository.findById(anyLong())).thenReturn(Optional.of(nc));
		when(validator.trasicaoValida(Mockito.any(), Mockito.any())).thenReturn(Boolean.TRUE);
		
		service.naoConformidadeMudaEstado(1L, Estado.EM_ANALISE);
		
		assertThat(nc.getEstado()).isEqualTo(Estado.EM_ANALISE);
	}
	
	@Test
	public void transicaoDeEstadoInvalida() {
		NaoConformidade nc = new NaoConformidade();
		Norma norma = new Norma();
		
		nc.setId(1L);
		nc.setEstado(Estado.ABERTA);
		norma.setNormaId(1L);
		
		when(repository.findById(anyLong())).thenReturn(Optional.of(nc));
		when(validator.trasicaoValida(Mockito.any(), Mockito.any())).thenReturn(Boolean.FALSE);
		
		assertThrows(IllegalStateException.class, () -> {
			service.naoConformidadeMudaEstado(1L, Estado.ABERTA);
		});
		
	}
	
	@Test
	public void associaNormaANC() {
		when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(new NaoConformidade()));
		service.associaNCANorma(1L, 1L);
	}
	
	@Test
	public void atualizaCheckList() {
		NaoConformidade nc = new NaoConformidade();
		Norma norma = new Norma();
		Map<String, Boolean> checklist = new HashMap<>();
		
		nc.setId(1L);
		nc.setEstado(Estado.ABERTA);
		norma.setNormaId(1L);
		nc.setNormaNaoConformidade(norma);
		
		checklist.put("1=1", Boolean.TRUE);
		
		when(repository.findById(anyLong())).thenReturn(Optional.of(nc));
		service.atualizaChecklist(1L, checklist);
		
		assertThat(nc.getNormaNaoConformidade().getCheckList()).isEqualTo(checklist);
	}
	
	@Test
	public void atualizaCheckListNormaInexistente() {
		NaoConformidade nc = new NaoConformidade();
		Norma norma = new Norma();
		Map<String, Boolean> checklist = new HashMap<>();
		
		nc.setId(1L);
		nc.setEstado(Estado.ABERTA);
		nc.setNormaNaoConformidade(norma);
		
		checklist.put("1=1", Boolean.TRUE);
		
		when(repository.findById(anyLong())).thenReturn(Optional.of(nc));
		
		assertThrows(IllegalStateException.class, () -> {
			service.atualizaChecklist(1L, checklist);
		});
		
	}
	
	@Test
	public void salvaNovaNaoConformidade() {
		NaoConformidadeTO ncTO = new NaoConformidadeTO();
		NaoConformidade nc = new NaoConformidade();
		nc.setId(1L);
		nc.setEstado(Estado.ABERTA);
		
		when(repository.findById(anyLong())).thenReturn(Optional.of(nc));
		when(repository.save(Mockito.any())).thenReturn(nc);
		assertThat(service.salvarNC(ncTO, 0L)).isEqualTo(1L);
		
	}
	
	@Test
	public void atualizaNaoConformidade() {
		NaoConformidadeTO ncTO = new NaoConformidadeTO();
		NaoConformidade nc = new NaoConformidade();
		nc.setId(1L);
		
		when(repository.findById(anyLong())).thenReturn(Optional.of(nc));
		when(repository.save(Mockito.any())).thenReturn(nc);
		assertThat(service.salvarNC(ncTO, 1L)).isEqualTo(1L);
	}
	
	@Test
	public void atualizaNaoConformidadeInexistente() {
		NaoConformidadeTO ncTO = new NaoConformidadeTO();

		assertThrows(EntityNotFoundException.class, () -> {
			service.salvarNC(ncTO, 1L);
		});
	}
	
	@Test
	public void atualizaNaoConformidadeEstadoInvalido() {
		NaoConformidadeTO ncTO = new NaoConformidadeTO();
		NaoConformidade nc = new NaoConformidade();
		
		nc.setId(1L);
		nc.setEstado(Estado.CONCLUIDA);
		
		when(repository.findById(anyLong())).thenReturn(Optional.of(nc));
		
		assertThrows(IllegalStateException.class, () -> {
			service.salvarNC(ncTO, 1L);
		});
	}
	
}
