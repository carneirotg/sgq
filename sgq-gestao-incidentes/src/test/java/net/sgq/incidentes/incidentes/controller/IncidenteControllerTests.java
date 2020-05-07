package net.sgq.incidentes.incidentes.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import net.sgq.incidentes.conformidades.modelos.enums.Estado;
import net.sgq.incidentes.conformidades.modelos.enums.Setor;
import net.sgq.incidentes.incidentes.modelos.Incidente;
import net.sgq.incidentes.incidentes.modelos.enums.ClassificacaoIncidente;
import net.sgq.incidentes.incidentes.modelos.enums.TipoIncidente;
import net.sgq.incidentes.incidentes.servicos.IncidenteService;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class IncidenteControllerTests {

	@MockBean
	private IncidenteService service;

	@Autowired
	private MockMvc mock;

	@Value("${sgq.test.token}")
	private String jwtToken;

	@BeforeEach
	public void init() {

		Incidente ic = new Incidente();
		ic.setSetor(Setor.COMPRAS);
		ic.setClassificacao(ClassificacaoIncidente.ALTO);
		ic.setTipoIncidente(TipoIncidente.DANO_FINANCEIRO);
		ic.setId(1L);
		ic.setSituacao(Estado.ABERTA);

		List<Incidente> listaIncidentes = new ArrayList<>();
		listaIncidentes.add(ic);
		
		PageImpl<Incidente> pageIC = new PageImpl<>(listaIncidentes);

		when(service.listaIncidentes(eq("Incidente 123"), any())).thenReturn(pageIC);
		when(service.listaIncidentes(eq("Incidente 456"), any())).thenReturn(new PageImpl<>(new ArrayList<>()));
		when(service.listaIncidentes(any(Estado.class), isNull(), any())).thenReturn(pageIC);
		when(service.listaIncidentes(any(Estado.class), eq(100), any())).thenReturn(pageIC);
		when(service.listaIncidentes(any(Estado.class), eq(1000), any())).thenReturn(new PageImpl<>(new ArrayList<>()));
		when(service.consultaIncidente(eq(1L))).thenReturn(ic);
		when(service.consultaIncidente(eq(2L))).thenReturn(null);
		when(service.salvarIncidente(any(), any())).thenReturn(1L);

	}

	@Test
	public void consultaIncidentes() throws Exception {

		when(service.listaIncidentes(any())).thenReturn(new PageImpl<>(new ArrayList<>()));
		
		mock.perform(setJwt(get("/incidentes"))).andExpect(status().isOk());
		mock.perform(setJwt(get("/incidentes?nome=Incidente 123"))).andExpect(status().isOk());
		mock.perform(setJwt(get("/incidentes?nome=Incidente 456"))).andExpect(status().isNotFound());
	}

	@Test
	public void consultaIncidente() throws Exception {

		mock.perform(setJwt(get("/incidentes/1"))).andExpect(status().isOk());
		mock.perform(setJwt(get("/incidentes/2"))).andExpect(status().isNotFound());

	}

	@Test
	public void listaEstados() throws Exception {

		mock.perform(setJwt(get("/incidentes?estado=abertos"))).andExpect(status().isOk());
		mock.perform(setJwt(get("/incidentes?estado=em_analise"))).andExpect(status().isOk());
		mock.perform(setJwt(get("/incidentes?estado=concluidos"))).andExpect(status().isOk());
		mock.perform(setJwt(get("/incidentes?estado=concluidos&janelaMinutos=100"))).andExpect(status().isOk());
		mock.perform(setJwt(get("/incidentes?estado=nao_concluidos"))).andExpect(status().isOk());

		mock.perform(setJwt(get("/incidentes?estado=concluidos&janelaMinutos=1000"))).andExpect(status().isNotFound());
	}

	@Test
	public void atualizaIncidente() throws Exception {

		String corpoValido = "{\"titulo\":\"Máquina Y baixa produtividade\",\"descricao\":\"Máquina Y está apresentando problemas.\",\"conclusao\":\"conclusao aqui\",\"setor\":\"LINHA\",\"classificacao\":\"MEDIO\",\"tipoIncidente\":\"DANO_FINANCEIRO\", \"situacao\": \"ABERTA\"}";
		String corpoInvalido = "{\"descricao\":\"Máquina Y está apresentando problemas quanto à sua produtividade visto a baixa potência da corrente que está ligada\",\"conclusao\":\"\",\"setor\":\"LINHA\",\"classificacao\":\"MEDIO\",\"tipoIncidente\":\"DANO_FINANCEIRO\"}";

		mock.perform(setJwt(put("/incidentes/1").contentType(MediaType.APPLICATION_JSON_VALUE).content(corpoValido)))
				.andExpect(status().isNoContent());
		mock.perform(setJwt(put("/incidentes/1").contentType(MediaType.APPLICATION_JSON_VALUE).content(corpoInvalido)))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void novoIncidente() throws Exception {

		String corpoValido = "{\"titulo\":\"Máquina Y baixa produtividade\",\"descricao\":\"Máquina Y está apresentando problemas.\",\"setor\":\"LINHA\",\"conclusao\":\"Ok\",\"classificacao\":\"MEDIO\",\"tipoIncidente\":\"DANO_FINANCEIRO\", \"situacao\": \"ABERTA\"}";
		String corpoInvalido = "{\"descricao\":\"Máquina Y está apresentando problemas quanto à sua produtividade visto a baixa potência da corrente que está ligada\",\"conclusao\":\"\",\"setor\":\"LINHA\",\"classificacao\":\"MEDIO\",\"tipoIncidente\":\"DANO_FINANCEIRO\"}";

		mock.perform(setJwt(post("/incidentes").contentType(MediaType.APPLICATION_JSON_VALUE).content(corpoValido)))
				.andExpect(status().isCreated()).andExpect(header().string("Location", "/incidentes/1"));
		mock.perform(setJwt(post("/incidentes").contentType(MediaType.APPLICATION_JSON_VALUE).content(corpoInvalido)))
				.andExpect(status().isBadRequest());
	}

	private MockHttpServletRequestBuilder setJwt(MockHttpServletRequestBuilder builder) {
		return builder.header("Authorization", "Bearer " + this.jwtToken);
	}

}
