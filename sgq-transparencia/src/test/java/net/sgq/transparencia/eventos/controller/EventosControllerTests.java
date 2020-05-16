package net.sgq.transparencia.eventos.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import net.sgq.transparencia.clientes.GestaoIncidentesClient;
import net.sgq.transparencia.clientes.to.IncidenteTO;

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
	
	@BeforeEach
	public void before() {
		@SuppressWarnings("unchecked")
		ResponseEntity<List<IncidenteTO>> mResponse = Mockito.mock(ResponseEntity.class);
		
		when(mResponse.getBody()).thenReturn(new ArrayList<>());
		when(mResponse.getHeaders()).thenReturn(Mockito.mock(HttpHeaders.class));
		when(mResponse.getStatusCodeValue()).thenReturn(200);
		
		when(client.consultaIncidentesConcluidos(Mockito.anyInt(), anyInt())).thenReturn(mResponse);
	}

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
