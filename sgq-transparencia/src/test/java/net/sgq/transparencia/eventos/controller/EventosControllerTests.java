package net.sgq.transparencia.eventos.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
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

import net.sgq.transparencia.clientes.GestaoIncidentesClient;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class EventosControllerTests {

	@MockBean
	private GestaoIncidentesClient client;

	@Autowired
	private MockMvc mock;

	@Value("${sgq.test.token}")
	private String jwtToken;

	@Test
	public void eventosEmJson() throws Exception {
		mock.perform(setJwt(get("/eventos").accept(MediaType.APPLICATION_JSON_VALUE)))
				.andExpect(header().string("Content-Type", "application/json")).andExpect(status().isOk());
	}
	
	@Test
	public void eventosEmXml() throws Exception {
		mock.perform(setJwt(get("/eventos").accept(MediaType.APPLICATION_XML_VALUE)))
				.andExpect(header().string("Content-Type", "application/xml")).andExpect(status().isOk());
	}
	
	private MockHttpServletRequestBuilder setJwt(MockHttpServletRequestBuilder builder) {
		return builder.header("Authorization", "Bearer " + this.jwtToken);
	}

}
