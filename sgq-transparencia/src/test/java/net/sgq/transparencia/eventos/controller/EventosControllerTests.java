package net.sgq.transparencia.eventos.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import net.sgq.transparencia.clientes.GestaoIncidentesClient;

@WebMvcTest(EventoController.class)
public class EventosControllerTests {

	@MockBean
	private GestaoIncidentesClient client;

	@Autowired
	private MockMvc mock;

	@Test
	public void eventosEmJson() throws Exception {
		mock.perform(get("/eventos").accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(header().string("Content-Type", "application/json")).andExpect(status().isOk());
	}
	
	@Test
	public void eventosEmXml() throws Exception {
		mock.perform(get("/eventos").accept(MediaType.APPLICATION_XML_VALUE))
				.andExpect(header().string("Content-Type", "application/xml")).andExpect(status().isOk());
	}

}
