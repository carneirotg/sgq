package net.sgq.transparencia.recall.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import net.sgq.transparencia.recall.modelos.to.CampanhaRecallTO;
import net.sgq.transparencia.recall.servicos.CampanhaService;

@WebMvcTest(CampanhaRecallController.class)
public class CampanhasRecallControllerTests {

	@MockBean
	private CampanhaService service;

	@Autowired
	private MockMvc mock;

	@Test
	public void novaCampanhaComLocationOk() throws Exception {
		when(service.salvar(any())).thenReturn(1L);
		mock.perform(post("/campanhas/").content("{}").contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(header().string("Location", "/campanhas/1"));
	}

	@Test
	public void listaTodasCampanhasSucesso() throws Exception {
		mock.perform(get("/campanhas")).andExpect(status().isOk());
	}

	@Test
	public void listaTodasCampanhasAtivasSucesso() throws Exception {
		preencheLista();
		mock.perform(get("/campanhas?estado=ativas")).andExpect(status().isOk());
	}

	@Test
	public void listaTodasCampanhasConcluidasSucesso() throws Exception {
		preencheLista();
		mock.perform(get("/campanhas?estado=concluidas")).andExpect(status().isOk());
	}

	@Test
	public void listaTodasCampanhasAtivasNaoEncontrado() throws Exception {
		mock.perform(get("/campanhas?estado=ativas")).andExpect(status().isNotFound());
	}

	@Test
	public void listaTodasCampanhasConcluidasNaoEncontrado() throws Exception {
		mock.perform(get("/campanhas?estado=concluidas")).andExpect(status().isNotFound());
	}

	@Test
	public void alteraDataTerminoComSucesso() throws Exception {
		mock.perform(patch("/campanhas/1/fim/2077-01-01")).andExpect(status().isNoContent());
	}

	@Test
	public void alteraDataTerminoSemSucessoAnterior() throws Exception {
		mock.perform(patch("/campanhas/1/fim/1800-01-01")).andExpect(status().isBadRequest());
	}

	public void alteraDataTerminoSemSucessoCampanhaConcluida() throws Exception {
		doThrow(IllegalStateException.class).when(service).atualizaDataTermino(any(), any());
		mock.perform(patch("/campanhas/1/fim/2077-01-01")).andExpect(status().isNoContent());
	}
	
	@Test
	public void concluiCampanhaComSucesso() throws Exception {
		mock.perform(patch("/campanhas/1/estado/concluida")).andExpect(status().isNoContent());
	}
	
	@Test
	public void concluiCampanhaSemSucessoJaConcluida() throws Exception {
		doThrow(IllegalStateException.class).when(service).concluiCampanha(any());
		mock.perform(patch("/campanhas/1/estado/concluida")).andExpect(status().isBadRequest());
	}
	
	@Test
	public void concluiCampanhaSemSucessoInexistente() throws Exception {
		doThrow(EntityNotFoundException.class).when(service).concluiCampanha(any());
		mock.perform(patch("/campanhas/1/estado/concluida")).andExpect(status().isNotFound());
	}
	
	private void preencheLista() {
		ArrayList<CampanhaRecallTO> mCampanhas = new ArrayList<>();
		mCampanhas.add(new CampanhaRecallTO());

		when(service.buscar(Mockito.any())).thenReturn(mCampanhas);
	}
}
