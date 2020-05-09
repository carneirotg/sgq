package net.sgq.incidentes.conformidades.servicos;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import net.sgq.incidentes.artefatos.modelos.Artefato;
import net.sgq.incidentes.artefatos.servicos.ArtefatoService;
import net.sgq.incidentes.clientes.GestaoNormasClient;
import net.sgq.incidentes.conformidades.modelos.NaoConformidade;
import net.sgq.incidentes.conformidades.modelos.NaoConformidadeRepository;
import net.sgq.incidentes.conformidades.modelos.Norma;
import net.sgq.incidentes.conformidades.modelos.enums.Estado;

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

	@Test
	public void serviceOk() {
		assertThat(service).isNotNull();
	}

	@Test
	public void listaNCs() {
		assertThat(service.listaNCs(Pageable.unpaged()).getContent()).isNotNull();
	}

	@Test
	public void listaNCsPorTitulo() {
		assertThat(service.listaNCs("abc", Pageable.unpaged()).getContent()).isNotNull();
	}

	@Test
	public void consultaNCsPorEstado() {
		List<NaoConformidade> ncs = new ArrayList<>();
		Artefato art = new Artefato();
		
		NaoConformidade nc = new NaoConformidade();

		art.setId(1L);
		nc.setArtefato(art);
		ncs.add(nc);

		Page<NaoConformidade> pageNC = new PageImpl<>(ncs);
		
		when(repository.findByEstado(any(), any())).thenReturn(pageNC);

		assertThat(service.listaNCs(Estado.ABERTA, Pageable.unpaged())).isNotNull().size().isEqualTo(1);
	}

	@Test
	public void consultaNCsNaoConcluidas() {
		List<NaoConformidade> ncs = new ArrayList<>();
		Artefato art = new Artefato();
		NaoConformidade nc = new NaoConformidade();

		art.setId(1L);
		nc.setArtefato(art);
		ncs.add(nc);
		
		Page<NaoConformidade> pageNC = new PageImpl<>(ncs);

		when(repository.findByEstadoNot(any(), any())).thenReturn(pageNC);

		assertThat(service.listaNCs(Estado.NAO_CONCLUIDA, Pageable.unpaged())).isNotNull().size().isEqualTo(1);
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

		assertThat(service.consultaNC(1L)).isNotNull();
	}

	@Test
	public void consultaEntidadeNCInexistente() {

		when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(null));

		assertThat(service.consultaNC(1L)).isNull();
	}

	@Test
	public void transicaoDeEstado() {
		NaoConformidade nc = new NaoConformidade();
		nc.setId(1L);
		nc.setEstado(Estado.ABERTA);

		when(repository.findById(anyLong())).thenReturn(Optional.of(nc));

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

		assertThrows(IllegalStateException.class, () -> {
			service.naoConformidadeMudaEstado(1L, Estado.ABERTA);
		});

	}

	@Test
	public void associaNormaANC() {
		when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(new NaoConformidade()));
		assertDoesNotThrow(() -> service.associaNCANorma(1L, 1L));
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
		NaoConformidade nc = new NaoConformidade();
		Artefato art = new Artefato();

		art.setId(1L);
		art.setDepreciado(Boolean.FALSE);

		nc.setId(1L);
		nc.setEstado(Estado.ABERTA);
		nc.setArtefato(art);

		when(repository.findById(anyLong())).thenReturn(Optional.of(nc));
		when(repository.save(any())).thenReturn(nc);
		when(artefatoService.buscaArtefatoPor(anyLong())).thenReturn(art);
		assertThat(service.salvarNC(nc, 0L)).isEqualTo(1L);

	}

	@Test
	public void atualizaNaoConformidade() {
		NaoConformidade nc = new NaoConformidade();
		nc.setId(1L);

		when(repository.findById(anyLong())).thenReturn(Optional.of(nc));
		when(repository.save(any())).thenReturn(nc);
		assertThat(service.salvarNC(nc, 1L)).isEqualTo(1L);
	}

	@Test
	public void atualizaNaoConformidadeInexistente() {
		NaoConformidade ncTO = new NaoConformidade();

		assertThrows(EntityNotFoundException.class, () -> {
			service.salvarNC(ncTO, 1L);
		});
	}

	@Test
	public void atualizaNaoConformidadeEstadoInvalido() {
		NaoConformidade nc = new NaoConformidade();

		nc.setId(1L);
		nc.setEstado(Estado.CONCLUIDA);

		when(repository.findById(anyLong())).thenReturn(Optional.of(nc));

		assertThrows(IllegalStateException.class, () -> {
			service.salvarNC(nc, 1L);
		});
	}

}
