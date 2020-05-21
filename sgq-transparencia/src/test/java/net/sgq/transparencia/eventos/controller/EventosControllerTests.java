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
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import net.sgq.transparencia.clientes.GestaoIncidentesClient;
import net.sgq.transparencia.clientes.to.IncidenteTO;
import net.sgq.transparencia.recall.modelos.to.CampanhaRecallTO;
import net.sgq.transparencia.recall.servicos.CampanhaService;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class EventosControllerTests {

	@MockBean
	private GestaoIncidentesClient client;
	
	@MockBean
	private CampanhaService campanhaService;

	@Autowired
	private MockMvc mock;

	@Value("${sgq.test.token}")
	private String jwtToken;
	
	@BeforeEach
	public void before() {
		@SuppressWarnings("unchecked")
		ResponseEntity<List<IncidenteTO>> mResponseIncidentes = Mockito.mock(ResponseEntity.class);
		
		when(mResponseIncidentes.getBody()).thenReturn(new ArrayList<>());
		when(mResponseIncidentes.getHeaders()).thenReturn(Mockito.mock(HttpHeaders.class));
		when(mResponseIncidentes.getStatusCodeValue()).thenReturn(200);
		
		List<CampanhaRecallTO> listaTO = new ArrayList<>();
		listaTO.add(new CampanhaRecallTO());
		
		PageImpl<CampanhaRecallTO> pageCampanhas = new PageImpl<>(listaTO); 
		
		when(client.consultaIncidentesConcluidos(anyInt(), anyInt())).thenReturn(mResponseIncidentes);
		when(campanhaService.buscar(Mockito.any(), Mockito.isNull(), Mockito.any())).thenReturn(pageCampanhas);
	}

	@Test
	public void eventosEmJsonIncidentes() throws Exception {
		mock.perform(setJwt(get("/eventos/incidentes").accept(MediaType.APPLICATION_JSON_VALUE)))
				.andExpect(header().string("Content-Type", "application/json")).andExpect(status().isOk());
	}
	
	@Test
	public void eventosEmXmlIncidentes() throws Exception {
		mock.perform(setJwt(get("/eventos/incidentes").accept(MediaType.APPLICATION_XML_VALUE)))
				.andExpect(header().string("Content-Type", "application/xml")).andExpect(status().isOk());
	}
	
	@Test
	public void eventosEmJsonCampanhas() throws Exception {
		mock.perform(setJwt(get("/eventos/campanhas").accept(MediaType.APPLICATION_JSON_VALUE)))
				.andExpect(header().string("Content-Type", "application/json")).andExpect(status().isOk());
	}
	
	@Test
	public void eventosEmXmlCampanhas() throws Exception {
		mock.perform(setJwt(get("/eventos/campanhas").accept(MediaType.APPLICATION_XML_VALUE)))
				.andExpect(header().string("Content-Type", "application/xml")).andExpect(status().isOk());
	}
	
	private MockHttpServletRequestBuilder setJwt(MockHttpServletRequestBuilder builder) {
		return builder.header("Authorization", "Bearer " + this.jwtToken);
	}

}
