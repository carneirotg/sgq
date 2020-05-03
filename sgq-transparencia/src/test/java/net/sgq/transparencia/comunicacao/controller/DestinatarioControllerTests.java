package net.sgq.transparencia.comunicacao.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import net.sgq.transparencia.comunicacao.servicos.DestinatarioService;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class DestinatarioControllerTests {

	@MockBean
	private DestinatarioService service;

	@Autowired
	private MockMvc mock;

	@Value("${sgq.test.token}")
	private String jwtToken;

	@Test
	public void retornaListaTodosComSucesso() throws Exception {
		mock.perform(setJwt(get("/destinatarios"))).andExpect(status().isOk());
	}

	@Test
	public void retornaCreatedParaUsuarioSalvoComSucesso() throws Exception {
		when(service.salvarDestinatario(any(), anyLong())).thenReturn(1L);

		mock.perform(setJwt(post("/destinatarios").content("{}").contentType(MediaType.APPLICATION_JSON_VALUE)))
				.andExpect(header().string("Location", "/destinatarios/1"));
	}
	
	@Test
	public void removeDestinatarioComSucesso() throws Exception {
		mock.perform(setJwt(delete("/destinatarios/1"))).andExpect(status().isNoContent());
	}
	
	@Test
	public void tentaRemoverDestinatarioInexistente() throws Exception {
		
		Mockito.doThrow(EntityNotFoundException.class).when(service).removeDestinatario(anyLong());
		
		mock.perform(setJwt(delete("/destinatarios/1"))).andExpect(status().isNotFound());
	}

	private MockHttpServletRequestBuilder setJwt(MockHttpServletRequestBuilder builder) {
		return builder.header("Authorization", "Bearer " + this.jwtToken);
	}
}
