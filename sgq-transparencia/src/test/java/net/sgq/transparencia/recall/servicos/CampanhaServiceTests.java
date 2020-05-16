package net.sgq.transparencia.recall.servicos;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import net.sgq.transparencia.recall.modelos.CampanhaRecall;
import net.sgq.transparencia.recall.modelos.CampanhaRecallRepository;
import net.sgq.transparencia.recall.modelos.InformacaoTecnica;
import net.sgq.transparencia.recall.modelos.NCRepository;
import net.sgq.transparencia.recall.modelos.enums.Estado;
import net.sgq.transparencia.recall.modelos.to.CampanhaRecallTO;

@SpringBootTest
public class CampanhaServiceTests {

	@Autowired
	private CampanhaService service;

	@MockBean
	private CampanhaRecallRepository repository;

	@MockBean
	private NCRepository ncRepository;

	@BeforeEach
	public void init() {

		CampanhaRecall c = newCampanhaRecall();
		CampanhaRecall d = newCampanhaRecall();
		CampanhaRecall e = newCampanhaRecall();
		CampanhaRecall f = newCampanhaRecall();

		c.setEstadoCampanha(Estado.ATIVA);
		e.setEstadoCampanha(Estado.ATIVA);
		d.setEstadoCampanha(Estado.CONCLUIDA);
		f.setEstadoCampanha(Estado.CONCLUIDA);

		Date hoje = new Date();
		Date ontem = Date
				.from(LocalDateTime.now().minus(1, ChronoUnit.DAYS).atZone(ZoneId.systemDefault()).toInstant());

		c.setInicio(hoje);
		e.setInicio(hoje);
		d.setInicio(ontem);
		f.setInicio(ontem);

		List<CampanhaRecall> todas = new ArrayList<>();
		List<CampanhaRecall> ativas = new ArrayList<>();
		List<CampanhaRecall> concluidas = new ArrayList<>();

		List<CampanhaRecall> todasJanela = new ArrayList<>();
		List<CampanhaRecall> ativasJanela = new ArrayList<>();
		List<CampanhaRecall> concluidasJanela = new ArrayList<>();

		todas.add(c);
		todas.add(d);

		ativas.add(c);
		concluidas.add(d);

		todasJanela.add(c);
		todasJanela.add(e);

		ativasJanela.add(e);
		concluidasJanela.add(f);

		Page<CampanhaRecall> pTodas = new PageImpl<>(todas);
		Page<CampanhaRecall> pAtivas = new PageImpl<>(ativas);
		Page<CampanhaRecall> pConcluidas = new PageImpl<>(concluidas);
		
		when(repository.findAll()).thenReturn(todas);
		when(repository.findByEstadoCampanha(eq(Estado.ATIVA))).thenReturn(ativas);
		when(repository.findByEstadoCampanha(eq(Estado.CONCLUIDA))).thenReturn(concluidas);
		when(repository.findAll(any(Pageable.class))).thenReturn(pTodas);
		when(repository.findByEstadoCampanha(eq(Estado.ATIVA), any(Pageable.class))).thenReturn(pAtivas);
		when(repository.findByEstadoCampanha(eq(Estado.CONCLUIDA), any(Pageable.class))).thenReturn(pConcluidas);
		when(repository.findByEstadoCampanhaAndInicioAfter(eq(Estado.ATIVA), any())).thenReturn(ativasJanela);
		when(repository.findByEstadoCampanhaAndInicioAfter(eq(Estado.CONCLUIDA), any())).thenReturn(concluidasJanela);
	}

	@Test
	public void buscarTodasCampanhas() {
		List<CampanhaRecallTO> campanhas = service.buscar(null, Pageable.unpaged()).getContent();

		assertThat(campanhas.size()).isEqualTo(2);
	}

	@Test
	public void buscarTodasCampanhasAtivas() {
		List<CampanhaRecallTO> campanhas = service.buscar(Estado.ATIVA, Pageable.unpaged()).getContent();

		assertThat(campanhas.size()).isEqualTo(1);
		assertThat(campanhas.get(0).getEstadoCampanha()).isEqualTo(Estado.ATIVA);
	}

	@Test
	public void buscarTodasCampanhasConcluidas() {
		List<CampanhaRecallTO> campanhas = service.buscar(Estado.CONCLUIDA, Pageable.unpaged()).getContent();

		assertThat(campanhas.size()).isEqualTo(1);
		assertThat(campanhas.get(0).getEstadoCampanha()).isEqualTo(Estado.CONCLUIDA);
	}

	@Test
	public void buscarTodasCampanhasDentroJanela() {
		List<CampanhaRecallTO> campanhas = service.buscar(null, 30);

		assertThat(campanhas.size()).isEqualTo(2);
	}

	@Test
	public void buscarTodasCampanhasAbertasDentroJanela() {
		List<CampanhaRecallTO> campanhas = service.buscar(Estado.ATIVA, 30);

		assertThat(campanhas.size()).isEqualTo(1);
		assertThat(campanhas.get(0).getEstadoCampanha()).isEqualTo(Estado.ATIVA);
	}

	@Test
	public void buscarTodasCampanhasConcluidasDentroJanela() {
		List<CampanhaRecallTO> campanhas = service.buscar(Estado.CONCLUIDA, 30);

		assertThat(campanhas.size()).isEqualTo(1);
		assertThat(campanhas.get(0).getEstadoCampanha()).isEqualTo(Estado.CONCLUIDA);
	}

	private CampanhaRecall newCampanhaRecall() {

		CampanhaRecall c = new CampanhaRecall();
		c.setInformacaoTecnica(new InformacaoTecnica());
		c.setNcsEnvolvidas(new ArrayList<>());

		return c;
	}

}
