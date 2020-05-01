package net.sgq.incidentes.conformidades.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import net.sgq.incidentes.conformidades.modelos.Norma;
import net.sgq.incidentes.conformidades.modelos.to.NaoConformidadeIdTO;
import net.sgq.incidentes.conformidades.servicos.NaoConformidadeService;
import net.sgq.incidentes.utils.EntityNotFoundException;

@WebMvcTest(controllers = NCNormaController.class)
public class NCNormasControllerTests {

	@MockBean
	private NaoConformidadeService service;

	@Autowired
	private MockMvc mock;

	@Test
	public void associaNorma() throws Exception {
		doNothing().when(service).associaNCANorma(anyLong(), anyLong());
		mock.perform(patch("/ncs/1/norma/1")).andExpect(status().isNoContent());
	}

	@Test
	public void associaNormaInexistente() throws Exception {
		doThrow(EntityNotFoundException.class).when(service).associaNCANorma(anyLong(), anyLong());

		mock.perform(patch("/ncs/1/norma/1")).andExpect(status().isBadRequest());
	}

	@Test
	public void buscaNormaAssociada() throws Exception {

		NaoConformidadeIdTO mNC = Mockito.mock(NaoConformidadeIdTO.class);
		when(service.consultaNC(anyLong())).thenReturn(mNC);
		when(mNC.getNorma()).thenReturn(new Norma());

		mock.perform(get("/ncs/1/norma/1")).andExpect(status().isOk());
	}

	@Test
	public void buscaNormaAssociadaInexistente() throws Exception {
		when(service.consultaNC(anyLong())).thenReturn(Mockito.mock(NaoConformidadeIdTO.class));

		mock.perform(get("/ncs/1/norma/1")).andExpect(status().isNotFound());
	}

	@Test
	public void atualizaChecklist() throws Exception {
		doNothing().when(service).atualizaChecklist(anyLong(), Mockito.any());
		mock.perform(patch("/ncs/1/norma/checklist").content("{}").contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isNoContent());
	}
	
	@Test
	public void atualizaCheckListNaoExistente() throws Exception {
		doThrow(IllegalStateException.class).when(service).atualizaChecklist(anyLong(), Mockito.any());
		mock.perform(patch("/ncs/1/norma/checklist").content("{}").contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isBadRequest());
	}

}
