package net.sgq.incidentes.artefatos.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import net.sgq.incidentes.artefatos.modelos.Artefato;
import net.sgq.incidentes.artefatos.servicos.ArtefatoService;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class ArtefatoControllerTests {

	@Autowired
	private MockMvc mock;
	
	@MockBean
	private ArtefatoService service;
	
	@Value("${sgq.test.token}")
	private String jwtToken;
	
	@Test
	public void listaArtefatosOK() throws Exception {
		@SuppressWarnings("unchecked")
		Page<Artefato> pageMock = Mockito.mock(Page.class);
		when(pageMock.getTotalPages()).thenReturn(100);
		
		when(service.buscaArtefatos(any(), any(), any())).thenReturn(pageMock);
		mock.perform(setJwt(get("/artefatos"))).andExpect(status().isOk());
	}
	
	@Test
	public void encontraArtefatoPorId() throws Exception {
		when(service.buscaArtefatoPor(any())).thenReturn(new Artefato());
		
		mock.perform(setJwt(get("/artefatos/1"))).andExpect(status().isOk());
	}
	
	@Test
	public void naoEncontraArtefatoPorId() throws Exception {
		mock.perform(setJwt(get("/artefatos/1"))).andExpect(status().isNotFound());
	}
	
	@Test
	public void encontraArtefatosPorNome() throws Exception {
		List<Artefato> artsList = new ArrayList<>();
		artsList.add(new Artefato());
		Page<Artefato> arts = new PageImpl<>(artsList);
		
		when(service.buscaArtefatos(any(), any(), any())).thenReturn(arts);
		
		mock.perform(setJwt(get("/artefatos?nome=abc"))).andExpect(status().isOk());
	}
	
	@Test
	public void naoEncontraArtefatosPorNome() throws Exception {
		@SuppressWarnings("unchecked")
		Page<Artefato> pageMock = Mockito.mock(Page.class);
		when(pageMock.getTotalPages()).thenReturn(100);
		
		when(service.buscaArtefatos(any(), any(), any())).thenReturn(pageMock);
		mock.perform(setJwt(get("/artefatos?nome=abc"))).andExpect(status().isNotFound());
	}
	
	@Test
	public void criaArtefatoValido() throws Exception {
		String artefato = "{\"nome\":\"abc\",\"descricao\":\"abc123\", \"urlImagem\": \"img.jpg\"}";
		when(service.salvaArtefato(any(), any())).thenReturn(1L);
		
		mock.perform(setJwt(post("/artefatos")).contentType(MediaType.APPLICATION_JSON_VALUE).content(artefato)).andExpect(status().isCreated());
		
	}
	
	@Test
	public void tentaCriarArtefatoInvalido() throws Exception {
		String artefato = "{\"nome\":\"ab\",\"descricao\":\"abc123\", \"urlImagem\": \"img.jpg\"}";
		when(service.salvaArtefato(any(), any())).thenReturn(1L);
		
		mock.perform(setJwt(post("/artefatos")).contentType(MediaType.APPLICATION_JSON_VALUE).content(artefato)).andExpect(status().isBadRequest());
		
	}
	
	@Test
	public void atualizaArtefato() throws Exception {
		String artefato = "{\"nome\":\"abc\",\"descricao\":\"abc123\", \"urlImagem\": \"img.jpg\"}";
		when(service.salvaArtefato(any(), any())).thenReturn(1L);
		
		mock.perform(setJwt(put("/artefatos/1")).contentType(MediaType.APPLICATION_JSON_VALUE).content(artefato)).andExpect(status().isNoContent());
		
	}
	
	@Test
	public void tentaAtualizarArtefatoInvalido() throws Exception {
		String artefato = "{\"nome\":\"ab\",\"descricao\":\"abc123\", \"urlImagem\": \"img.jpg\"}";
		when(service.salvaArtefato(any(), any())).thenReturn(1L);
		
		mock.perform(setJwt(put("/artefatos/1").contentType(MediaType.APPLICATION_JSON_VALUE).content(artefato))).andExpect(status().isBadRequest());
	}

	@Test
	public void depreciaArtefato() throws Exception {
		mock.perform(setJwt(patch("/artefatos/1/depreciado"))).andExpect(status().isNoContent());
	}

	private MockHttpServletRequestBuilder setJwt(MockHttpServletRequestBuilder builder) {
		return builder.header("Authorization", "Bearer " + this.jwtToken);
	}
}
