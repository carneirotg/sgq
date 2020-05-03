package net.sgq.gateway.normas.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import net.sgq.gateway.normas.modelos.Norma;
import net.sgq.gateway.normas.servicos.NormaService;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class NormaControllerTests {

	@Autowired
	private MockMvc mock;
	
	@MockBean	
	private JwtDecoder jwtDecoder;
	
	@MockBean
	private NormaService service;
	
	@Value("${sgq.test.token}")
	private String jwtToken;

	@Test
	public void normaControllerOk() {
		assertThat(service).isNotNull();
	}
	
	@Test
	public void listaNormasOk() throws Exception {
		this.listaUmaNorma();
		
		mock.perform(setJwt(get("/normas"))).andExpect(status().isOk());
	}
	
	@Test
	public void listaNormasRetornoNuloSistemaExterno() throws Exception {
		this.listaNormaNula();
		
		mock.perform(setJwt(get("/normas"))).andExpect(status().isBadGateway());
	}
	
	@Test
	public void listaNormasVazias() throws Exception {
		mock.perform(setJwt(get("/normas"))).andExpect(status().isNotFound());
	}
	
	@Test
	public void consultaNormaOk() throws Exception {
		when(service.consultaNorma(Mockito.anyLong())).thenReturn(new Norma());
		mock.perform(setJwt(get("/normas/1"))).andExpect(status().isOk());
	}
	
	@Test
	public void listaNormasInexistente() throws Exception {
		when(service.consultaNorma(Mockito.anyLong())).thenReturn(null);
		mock.perform(setJwt(get("/normas/1"))).andExpect(status().isNotFound());
	}
	
	private void listaUmaNorma() {
		List<Norma> listaNorma = new ArrayList<>();
		listaNorma.add(new Norma());
		
		when(service.listaNormas()).thenReturn(listaNorma);
	}
	
	private void listaNormaNula() {
		when(service.listaNormas()).thenReturn(null);
	}
	
	private MockHttpServletRequestBuilder setJwt(MockHttpServletRequestBuilder builder) {
		return builder.header("Authorization", "Bearer " + this.jwtToken);
	}
	
}
