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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import net.sgq.transparencia.comunicacao.servicos.DestinatarioService;

@WebMvcTest(DestinatarioController.class)
public class DestinatarioControllerTests {

	@MockBean
	private DestinatarioService service;

	@Autowired
	private MockMvc mock;

	@Test
	public void retornaListaTodosComSucesso() throws Exception {
		mock.perform(get("/destinatarios")).andExpect(status().isOk());
	}

	@Test
	public void retornaCreatedParaUsuarioSalvoComSucesso() throws Exception {
		when(service.salvarDestinatario(any(), anyLong())).thenReturn(1L);

		mock.perform(post("/destinatarios").content("{}").contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(header().string("Location", "/destinatarios/1"));
	}
	
	@Test
	public void removeDestinatarioComSucesso() throws Exception {
		mock.perform(delete("/destinatarios/1")).andExpect(status().isNoContent());
	}
	
	@Test
	public void tentaRemoverDestinatarioInexistente() throws Exception {
		
		Mockito.doThrow(EntityNotFoundException.class).when(service).removeDestinatario(anyLong());
		
		mock.perform(delete("/destinatarios/1")).andExpect(status().isNotFound());
	}

}
