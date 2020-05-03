package net.sgq.relatorios.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import net.sgq.relatorios.servicos.RelIncidentesService;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class RelIncidentesControllerTests {

	@Autowired
	private MockMvc mock;
	
	@MockBean
	private RelIncidentesService service;
	
	@Value("${sgq.test.token}")
	private String jwtToken;
	
	@Test
	public void periodoInvalidoDeRelatorio() throws Exception {
		mock.perform(setJwt(get("/relatorios/incidentes/de/2020-05-01/ate/2020-04-01"))).andExpect(status().isBadRequest());
	}
	
	@Test
	public void periodoMaiorDeRelatorio() throws Exception {
		mock.perform(setJwt(get("/relatorios/incidentes/de/2010-05-01/ate/2020-04-01"))).andExpect(status().isBadRequest());
	}
	
	@Test
	public void relatorioNuloGerado() throws Exception {
		when(service.geraRelatorioPor(Mockito.any(), Mockito.any())).thenReturn(null);
		mock.perform(setJwt(get("/relatorios/incidentes/de/2020-02-01/ate/2020-04-01"))).andExpect(status().isInternalServerError());
	}
	
	private MockHttpServletRequestBuilder setJwt(MockHttpServletRequestBuilder builder) {
		return builder.header("Authorization", "Bearer " + this.jwtToken);
	}
	
}
