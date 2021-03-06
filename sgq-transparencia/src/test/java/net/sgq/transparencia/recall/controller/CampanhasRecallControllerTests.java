package net.sgq.transparencia.recall.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang.time.DateUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import net.sgq.transparencia.recall.modelos.enums.Estado;
import net.sgq.transparencia.recall.modelos.to.CampanhaRecallTO;
import net.sgq.transparencia.recall.servicos.CampanhaService;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class CampanhasRecallControllerTests {

	@MockBean
	private CampanhaService service;

	@Autowired
	private MockMvc mock;

	@Value("${sgq.test.token}")
	private String jwtToken;
	
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

	@Test
	public void novaCampanhaComLocationOk() throws Exception {
		when(service.salvar(any())).thenReturn(1L);
		
		Date dt = new Date();
		String dtInicio = SDF.format(dt);
		String dtFim = SDF.format(DateUtils.addDays(dt, 60));
		
		mock.perform(setJwt(post("/campanhas/").content(
				"{\"titulo\":\"Máquina Y baixa produtividade\",\"inicio\":\""+ dtInicio +"\",\"fim\":\"" + dtFim +"\",\"informativoCampanha\":\"Informativo da campanha de recall\",\"medidasCorretivas\":\"Medidas corretivas da campanha de recall\",\"incidentesConhecidos\":false,\"dataConstatacao\":\"2020-01-01\",\"defeito\":\"Defeito apontado na campanha\",\"tipoRisco\":\"VARIOS\",\"ncsEnvolvidas\":[{\"id\":1,\"titulo\":\"Titulo na NC envolvida na campanha\"}],\"estadoCampanha\":\"ATIVA\"}")
				.contentType(MediaType.APPLICATION_JSON_VALUE))).andExpect(status().isCreated())
				.andExpect(header().string("Location", "/campanhas/1"));
	}

	@Test
	public void listaTodasCampanhasSucesso() throws Exception {
		when(service.buscar(isNull(), isNull(), any(Pageable.class))).thenReturn(new PageImpl<>(new ArrayList<>()));
		mock.perform(setJwt(get("/campanhas"))).andExpect(status().isOk());
	}

	@Test
	public void listaTodasCampanhasAtivasSucesso() throws Exception {
		preencheLista();
		mock.perform(setJwt(get("/campanhas?estado=ativas"))).andExpect(status().isOk());
	}

	@Test
	public void listaTodasCampanhasConcluidasSucesso() throws Exception {
		preencheLista();
		mock.perform(setJwt(get("/campanhas?estado=concluidas"))).andExpect(status().isOk());
	}

	@Test
	public void listaTodasCampanhasAtivasNaoEncontrado() throws Exception {
		when(service.buscar(Mockito.eq(Estado.ATIVA), isNull(), any(Pageable.class)))
				.thenReturn(new PageImpl<>(new ArrayList<>()));
		mock.perform(setJwt(get("/campanhas?estado=ativas"))).andExpect(status().isNotFound());
	}

	@Test
	public void listaTodasCampanhasConcluidasNaoEncontrado() throws Exception {
		when(service.buscar(Mockito.eq(Estado.CONCLUIDA), isNull(), any(Pageable.class)))
				.thenReturn(new PageImpl<>(new ArrayList<>()));
		mock.perform(setJwt(get("/campanhas?estado=concluidas"))).andExpect(status().isNotFound());
	}

	@Test
	public void alteraDataTerminoComSucesso() throws Exception {
		mock.perform(setJwt(patch("/campanhas/1/fim/2077-01-01"))).andExpect(status().isNoContent());
	}

	@Test
	public void alteraDataTerminoSemSucessoAnterior() throws Exception {
		mock.perform(setJwt(patch("/campanhas/1/fim/1800-01-01"))).andExpect(status().isBadRequest());
	}

	public void alteraDataTerminoSemSucessoCampanhaConcluida() throws Exception {
		doThrow(IllegalStateException.class).when(service).atualizaDataTermino(any(), any());
		mock.perform(setJwt(patch("/campanhas/1/fim/2077-01-01"))).andExpect(status().isNoContent());
	}

	@Test
	public void concluiCampanhaComSucesso() throws Exception {
		mock.perform(setJwt(patch("/campanhas/1/estado/concluida"))).andExpect(status().isNoContent());
	}

	@Test
	public void concluiCampanhaSemSucessoJaConcluida() throws Exception {
		doThrow(IllegalStateException.class).when(service).concluiCampanha(any());
		mock.perform(setJwt(patch("/campanhas/1/estado/concluida"))).andExpect(status().isBadRequest());
	}

	@Test
	public void concluiCampanhaSemSucessoInexistente() throws Exception {
		doThrow(EntityNotFoundException.class).when(service).concluiCampanha(any());
		mock.perform(setJwt(patch("/campanhas/1/estado/concluida"))).andExpect(status().isNotFound());
	}

	private void preencheLista() {
		ArrayList<CampanhaRecallTO> mCampanhas = new ArrayList<>();
		mCampanhas.add(new CampanhaRecallTO());

		PageImpl<CampanhaRecallTO> pCampanhas = new PageImpl<>(mCampanhas);

		when(service.buscar(any(), isNull(), any(Pageable.class))).thenReturn(pCampanhas);
	}

	private MockHttpServletRequestBuilder setJwt(MockHttpServletRequestBuilder builder) {
		return builder.header("Authorization", "Bearer " + this.jwtToken);
	}
}
