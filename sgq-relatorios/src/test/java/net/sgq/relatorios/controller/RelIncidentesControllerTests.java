package net.sgq.relatorios.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import net.sgq.relatorios.servicos.RelIncidentesService;

@WebMvcTest(controllers = RelIncidentesController.class)
public class RelIncidentesControllerTests {

	@Autowired
	private MockMvc mock;
	
	@MockBean
	private RelIncidentesService service;
	
	@Test
	public void periodoInvalidoDeRelatorio() throws Exception {
		mock.perform(get("/relatorios/incidentes/de/2020-05-01/ate/2020-04-01")).andExpect(status().isBadRequest());
	}
	
	@Test
	public void periodoMaiorDeRelatorio() throws Exception {
		mock.perform(get("/relatorios/incidentes/de/2010-05-01/ate/2020-04-01")).andExpect(status().isBadRequest());
	}
	
	@Test
	public void relatorioNuloGerado() throws Exception {
		when(service.geraRelatorioPor(Mockito.any(), Mockito.any())).thenReturn(null);
		mock.perform(get("/relatorios/incidentes/de/2020-02-01/ate/2020-04-01")).andExpect(status().isInternalServerError());
	}
	
}
