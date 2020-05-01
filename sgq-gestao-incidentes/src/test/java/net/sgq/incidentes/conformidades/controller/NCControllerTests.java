package net.sgq.incidentes.conformidades.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import net.sgq.incidentes.conformidades.modelos.enums.Estado;
import net.sgq.incidentes.conformidades.modelos.enums.Setor;
import net.sgq.incidentes.conformidades.modelos.enums.TipoNaoConformidade;
import net.sgq.incidentes.conformidades.modelos.to.NaoConformidadeIdTO;
import net.sgq.incidentes.conformidades.servicos.NaoConformidadeService;

@WebMvcTest(controllers = NCController.class)
public class NCControllerTests {

	@MockBean
	private NaoConformidadeService service;

	@Autowired
	private MockMvc mock;

	@Test
	public void listaNCS() throws Exception {
		mock.perform(get("/ncs")).andExpect(status().isOk());
	}

	@Test
	public void listaNCSNome() throws Exception {
		preencheListaDeNCS();

		mock.perform(get("/ncs?nome=a")).andExpect(status().isOk());
	}

	@Test
	public void listaNCSNomeNaoEncontrado() throws Exception {
		mock.perform(get("/ncs?nome=a")).andExpect(status().isNotFound());
	}

	@Test
	public void consultaNCId() throws Exception {
		when(service.consultaNC(Mockito.anyLong())).thenReturn(criaNC());

		mock.perform(get("/ncs/1")).andExpect(status().isOk());
	}

	@Test
	public void consultaNCIdNaoEncontrado() throws Exception {
		mock.perform(get("/ncs/1")).andExpect(status().isNotFound());
	}

	@Test
	public void consultaNCAbertas() throws Exception {
		preencheListaDeNCS();
		mock.perform(get("/ncs?estado=abertas")).andExpect(status().isOk());
	}

	@Test
	public void consultaNCEmAnalise() throws Exception {
		preencheListaDeNCS();
		mock.perform(get("/ncs?estado=em_analise")).andExpect(status().isOk());
	}

	@Test
	public void consultaNCConcluidas() throws Exception {
		preencheListaDeNCS();
		mock.perform(get("/ncs?estado=concluidas")).andExpect(status().isOk());
	}

	@Test
	public void consultaNCNaoConcluidas() throws Exception {
		preencheListaDeNCS();
		mock.perform(get("/ncs?estado=nao_concluidas")).andExpect(status().isOk());
	}

	@Test
	public void consultaNCQualquerEstadoNaoEncontrada() throws Exception {
		mock.perform(get("/ncs?estado=nao_concluidas")).andExpect(status().isNotFound());
	}

	@Test
	public void novaNC() throws Exception {
		when(service.salvarNC(any(), any())).thenReturn(1L);
		mock.perform(post("/ncs").contentType(MediaType.APPLICATION_JSON_VALUE).content("{}"))
				.andExpect(status().isCreated()).andExpect(header().string("Location", "/ncs/1"));
	}

	@Test
	public void atualizaNCValida() throws Exception {
		mock.perform(put("/ncs/1").contentType(MediaType.APPLICATION_JSON_VALUE).content("{}"))
				.andExpect(status().isNoContent());
	}

	private void preencheListaDeNCS() {
		List<NaoConformidadeIdTO> list = new ArrayList<>();
		list.add(criaNC());
		when(service.listaNCs(anyString())).thenReturn(list);
		when(service.listaNCs(any(Estado.class))).thenReturn(list);
	}

	private NaoConformidadeIdTO criaNC() {
		return new NaoConformidadeIdTO("", TipoNaoConformidade.AUSENCIA_MEDIDA, "", "", Setor.COMPRAS, 1L, "",
				Boolean.TRUE, 1L, Estado.ABERTA, null);
	}

}
