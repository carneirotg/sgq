package net.sgq.incidentes.conformidades.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import net.sgq.incidentes.conformidades.modelos.NaoConformidade;
import net.sgq.incidentes.conformidades.modelos.Norma;
import net.sgq.incidentes.conformidades.servicos.NaoConformidadeService;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class NCNormasControllerTests {

	@MockBean
	private NaoConformidadeService service;

	@Autowired
	private MockMvc mock;

	@Value("${sgq.test.token}")
	private String jwtToken;

	@Test
	public void associaNorma() throws Exception {
		doNothing().when(service).associaNCANorma(anyLong(), anyLong());
		mock.perform(setJwt(patch("/ncs/1/norma/1"))).andExpect(status().isNoContent());
	}

	@Test
	public void associaNormaInexistente() throws Exception {
		doThrow(EntityNotFoundException.class).when(service).associaNCANorma(anyLong(), anyLong());

		mock.perform(setJwt(patch("/ncs/1/norma/1"))).andExpect(status().isBadRequest());
	}

	@Test
	public void buscaNormaAssociada() throws Exception {

		NaoConformidade mNC = Mockito.mock(NaoConformidade.class);
		when(service.consultaNC(anyLong())).thenReturn(mNC);
		when(mNC.getNormaNaoConformidade()).thenReturn(new Norma());

		mock.perform(setJwt(get("/ncs/1/norma/1"))).andExpect(status().isOk());
	}

	@Test
	public void buscaNormaAssociadaInexistente() throws Exception {
		when(service.consultaNC(anyLong())).thenReturn(Mockito.mock(NaoConformidade.class));

		mock.perform(setJwt(get("/ncs/1/norma/1"))).andExpect(status().isNotFound());
	}

	@Test
	public void atualizaChecklist() throws Exception {
		doNothing().when(service).atualizaChecklist(anyLong(), Mockito.any());
		mock.perform(setJwt(patch("/ncs/1/norma/checklist").content("{}").contentType(MediaType.APPLICATION_JSON_VALUE)))
				.andExpect(status().isNoContent());
	}
	
	@Test
	public void atualizaCheckListNaoExistente() throws Exception {
		doThrow(IllegalStateException.class).when(service).atualizaChecklist(anyLong(), Mockito.any());
		mock.perform(setJwt(patch("/ncs/1/norma/checklist").content("{}").contentType(MediaType.APPLICATION_JSON_VALUE)))
				.andExpect(status().isBadRequest());
	}
	
	private MockHttpServletRequestBuilder setJwt(MockHttpServletRequestBuilder builder) {
		return builder.header("Authorization", "Bearer " + this.jwtToken);
	}

}
