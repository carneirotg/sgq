package net.sgq.incidentes.artefatos.servicos;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import net.sgq.incidentes.artefatos.modelos.Artefato;
import net.sgq.incidentes.artefatos.modelos.ArtefatoRepository;

@SpringBootTest
public class ArtefatosServiceTests {

	@Autowired
	private ArtefatoService service;

	@MockBean
	private ArtefatoRepository repository;

	@Test
	public void serviceOk() {
		assertThat(service).isNotNull();
	}

	@Test
	public void buscaArtefatoPorId() {
		when(repository.findById(anyLong())).thenReturn(Optional.of(new Artefato()));

		assertThat(service.buscaArtefatoPor(1L)).isNotNull();
	}

	@Test
	public void buscaArtefatoPorIdInexistente() {
		when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(null));

		assertThat(service.buscaArtefatoPor(1L)).isNull();
	}

	@Test
	public void buscaArtefatos() {
		List<Artefato> arts = new ArrayList<>();
		arts.add(new Artefato());
		when(repository.findAll(Mockito.any(PageRequest.class))).thenReturn(new PageImpl<>(arts));

		Page<Artefato> artefatos = service.buscaArtefatos(null, 1, 1);

		assertThat(artefatos).isNotNull().isNotEmpty();
	}

	@Test
	public void buscaArtefatosPorNome() {
		List<Artefato> artsList = new ArrayList<>();
		artsList.add(new Artefato());
		
		Page<Artefato> arts = new PageImpl<>(artsList);

		when(repository.findByNomeContainingIgnoreCase(Mockito.anyString(), Mockito.any())).thenReturn(arts);

		Page<Artefato> artefatos = service.buscaArtefatos("nome", 1, 1);

		assertThat(artefatos).isNotNull().isNotEmpty();
	}

	@Test
	public void salvaArtefato() {

		Artefato a = new Artefato();
		a.setId(1L);

		when(repository.save(Mockito.any())).thenReturn(a);

		Long id = service.salvaArtefato(new Artefato(), 0L);

		assertThat(id).isEqualTo(1);

	}

	@Test
	public void atualizaArtefato() {

		Artefato a1 = criaArtefatoConsultavel(Boolean.FALSE);
		when(repository.save(Mockito.any())).thenReturn(a1);

		assertThat(service.salvaArtefato(new Artefato(), 1L)).isEqualTo(1L);

	}

	@Test
	public void tentaAtualizarArtefatoInexistente() {

		assertThrows(EntityNotFoundException.class, () -> {
			service.salvaArtefato(new Artefato(), 1L);
		});

	}

	@Test
	public void tentaAtualizarArtefatoDepreciado() {
		criaArtefatoConsultavel(Boolean.TRUE);
		assertThrows(IllegalStateException.class, () -> {
			service.salvaArtefato(new Artefato(), 1L);
		});
	}

	private Artefato criaArtefatoConsultavel(Boolean depreciado) {
		Artefato a1 = new Artefato();
		a1.setDepreciado(depreciado);
		a1.setDescricao("Abc");
		a1.setNome("Abcdefg");
		a1.setUrlImagem("imagem.png");
		a1.setId(1L);

		when(repository.findById(Mockito.any())).thenReturn(Optional.of(a1));
		return a1;
	}

}
