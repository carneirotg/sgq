package net.sgq.incidentes.artefatos.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import net.sgq.incidentes.artefatos.modelos.to.ArtefatoIdTO;
import net.sgq.incidentes.artefatos.servicos.ArtefatoService;

@WebMvcTest(controllers = ArtefatoController.class)
public class ArtefatoControllerTests {

	@Autowired
	private MockMvc mock;
	
	@MockBean
	private ArtefatoService service;
	
	@Test
	public void listaArtefatosOK() throws Exception {
		mock.perform(get("/artefatos")).andExpect(status().isOk());
	}
	
	@Test
	public void encontraArtefatoPorId() throws Exception {
		when(service.buscaArtefatoPor(any())).thenReturn(new ArtefatoIdTO());
		
		mock.perform(get("/artefatos/1")).andExpect(status().isOk());
	}
	
	@Test
	public void naoEncontraArtefatoPorId() throws Exception {
		mock.perform(get("/artefatos/1")).andExpect(status().isNotFound());
	}
	
	@Test
	public void encontraArtefatosPorNome() throws Exception {
		ArrayList<ArtefatoIdTO> arts = new ArrayList<>();
		arts.add(new ArtefatoIdTO());
		
		when(service.buscaArtefatos(any(), any(), any())).thenReturn(arts);
		
		mock.perform(get("/artefatos?nome=abc")).andExpect(status().isOk());
	}
	
	@Test
	public void naoEncontraArtefatosPorNome() throws Exception {
		mock.perform(get("/artefatos?nome=abc")).andExpect(status().isNotFound());
	}
	
	@Test
	public void criaArtefatoValido() throws Exception {
		String artefato = "{\"nome\":\"abc\",\"descricao\":\"abc123\", \"urlImagem\": \"img.jpg\"}";
		when(service.salvaArtefato(any(), any())).thenReturn(1L);
		
		mock.perform(post("/artefatos").contentType(MediaType.APPLICATION_JSON_VALUE).content(artefato)).andExpect(status().isCreated());
		
	}
	
	@Test
	public void tentaCriarArtefatoInvalido() throws Exception {
		String artefato = "{\"nome\":\"ab\",\"descricao\":\"abc123\", \"urlImagem\": \"img.jpg\"}";
		when(service.salvaArtefato(any(), any())).thenReturn(1L);
		
		mock.perform(post("/artefatos").contentType(MediaType.APPLICATION_JSON_VALUE).content(artefato)).andExpect(status().isBadRequest());
		
	}
	
	@Test
	public void atualizaArtefato() throws Exception {
		String artefato = "{\"nome\":\"abc\",\"descricao\":\"abc123\", \"urlImagem\": \"img.jpg\"}";
		when(service.salvaArtefato(any(), any())).thenReturn(1L);
		
		mock.perform(put("/artefatos/1").contentType(MediaType.APPLICATION_JSON_VALUE).content(artefato)).andExpect(status().isNoContent());
		
	}
	
	@Test
	public void tentaAtualizarArtefatoInvalido() throws Exception {
		String artefato = "{\"nome\":\"ab\",\"descricao\":\"abc123\", \"urlImagem\": \"img.jpg\"}";
		when(service.salvaArtefato(any(), any())).thenReturn(1L);
		
		mock.perform(put("/artefatos/1").contentType(MediaType.APPLICATION_JSON_VALUE).content(artefato)).andExpect(status().isBadRequest());
	}

	@Test
	public void depreciaArtefato() throws Exception {
		mock.perform(patch("/artefatos/1/depreciado")).andExpect(status().isNoContent());
	}
	
}
