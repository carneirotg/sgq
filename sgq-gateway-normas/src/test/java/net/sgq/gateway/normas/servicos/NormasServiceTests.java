package net.sgq.gateway.normas.servicos;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import net.sgq.gateway.normas.cliente.GestaoNormaClient;
import net.sgq.gateway.normas.modelos.Norma;

@SpringBootTest
public class NormasServiceTests {

	@Autowired
	private NormaService service;
	
	@MockBean
	private GestaoNormaClient client; 
	
	@BeforeEach
	public void beforeEach() {
		List<Norma> normas = new ArrayList<>();
		Norma norma = new Norma();
		
		normas.add(norma);
		
		when(client.consultaListaNormas()).thenReturn(normas);
		when(client.consultaNorma(Mockito.anyLong())).thenReturn(norma);
	}
	
	@Test
	public void init() {
		assertThat(service).isNotNull();
	}
	
	@Test
	public void listaNormasSucesso() {
		List<Norma> normas = service.listaNormas();
		
		assertThat(normas).isNotNull();
		assertThat(normas.size()).isEqualTo(1);
	}
	
	@Test
	public void listaNormasErro() {
		this.retornosNulos();
		List<Norma> normas = service.listaNormas();
		
		assertThat(normas).isNull();
	}
	
	@Test
	public void consultaNormaSucesso() {
		Norma normas = service.consultaNorma(1L);
		
		assertThat(normas).isNotNull();
	}
	
	@Test
	public void consultaNormaErro() {
		this.retornosNulos();
		Norma normas = service.consultaNorma(1L);
		
		assertThat(normas).isNull();
	}
	
	private void retornosNulos() {
		when(client.consultaListaNormas()).thenReturn(null);
		when(client.consultaNorma(Mockito.anyLong())).thenReturn(null);
	}
	
}
