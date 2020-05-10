package net.sgq.incidentes.conformidades.controller;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import net.sgq.incidentes.conformidades.modelos.enums.Estado;
import net.sgq.incidentes.conformidades.servicos.NaoConformidadeService;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class NCEstadoControllerTests {

	@MockBean
	private NaoConformidadeService service;
	
	@Autowired
	private MockMvc mock;

	@Value("${sgq.test.token}")
	private String jwtToken;
	
	@Test
	public void mudaEstadoAbertaOk() throws Exception {
		doNothing();
		mock.perform(setJwt(patch("/ncs/1/estado/aberta"))).andExpect(status().isNoContent());
	}
	
	@Test
	public void mudaEstadoEmAnaliseOk() throws Exception {
		doNothing();
		mock.perform(setJwt(patch("/ncs/1/estado/em_analise"))).andExpect(status().isNoContent());
	}
	
	@Test
	public void mudaEstadoConcluida() throws Exception {
		doNothing();
		mock.perform(setJwt(patch("/ncs/1/estado/concluida"))).andExpect(status().isNoContent());
	}
	
	@Test
	public void mudaEstadoInvalidoAberta() throws Exception {
		transicaoInvalida();
		mock.perform(setJwt(patch("/ncs/1/estado/aberta"))).andExpect(status().isBadRequest());
	}
	
	@Test
	public void mudaEstadoConcluidaInexistente() throws Exception {
		ncInexistente();
		mock.perform(setJwt(patch("/ncs/1/estado/concluida"))).andExpect(status().isBadRequest());
	}
	
	private void doNothing() {
		Mockito.doNothing().when(service).naoConformidadeMudaEstado(Mockito.anyLong(), Mockito.any(Estado.class));
	}
	
	private void transicaoInvalida() {
		doThrow(IllegalStateException.class).when(service).naoConformidadeMudaEstado(Mockito.anyLong(), Mockito.any(Estado.class));
	}
	
	private void ncInexistente() {
		doThrow(EntityNotFoundException.class).when(service).naoConformidadeMudaEstado(Mockito.anyLong(), Mockito.any(Estado.class));
	}
	
	private MockHttpServletRequestBuilder setJwt(MockHttpServletRequestBuilder builder) {
		return builder.header("Authorization", "Bearer " + this.jwtToken);
	}
	
}
