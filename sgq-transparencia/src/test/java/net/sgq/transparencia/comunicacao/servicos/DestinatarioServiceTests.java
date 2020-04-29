package net.sgq.transparencia.comunicacao.servicos;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import net.sgq.transparencia.comunicacao.modelos.Destinatario;
import net.sgq.transparencia.comunicacao.modelos.DestinatarioRepository;
import net.sgq.transparencia.comunicacao.modelos.to.DestinatarioTO;

@SpringBootTest
public class DestinatarioServiceTests {

	@Autowired
	private DestinatarioService service;

	@MockBean
	private DestinatarioRepository repository;
	
	@BeforeEach
	public void init() {
		
		Destinatario d = new Destinatario();
		Destinatario e = new Destinatario();
		
		d.setAssinanteEventos(Boolean.FALSE);
		d.setAssinanteRecall(Boolean.TRUE);
		
		e.setAssinanteEventos(Boolean.TRUE);
		e.setAssinanteRecall(Boolean.FALSE);
		
		List<Destinatario> destinatariosTodos = new ArrayList<>();
		destinatariosTodos.add(d);
		destinatariosTodos.add(e);
		
		List<Destinatario> destinatariosRecall = new ArrayList<>();
		destinatariosRecall.add(d);
		
		List<Destinatario> destinatariosEventos = new ArrayList<>();
		destinatariosEventos.add(e);
		
		when(repository.findAll()).thenReturn(destinatariosTodos);
		when(repository.findByAssinanteEventosIsTrue()).thenReturn(destinatariosEventos);
		when(repository.findByAssinanteRecallIsTrue()).thenReturn(destinatariosRecall);
		
		when(repository.findById(171L)).thenReturn(Optional.ofNullable(null));
	}

	@Test
	public void destinarioServiceOk() {
		assertThat(service).isNotNull();
	}
	
	@Test
	public void todosDestinarios() {
		assertThat(service.todos()).size().isEqualTo(2);
	}
	
	@Test
	public void todosDestinariosRecall() {
		assertThat(service.interessadosRecall()).size().isEqualTo(1);
	}
	
	@Test
	public void todosDestinariosEventos() {
		assertThat(service.interessadosIncidentes()).size().isEqualTo(1);
	}
	
	@Test
	public void tentaRemoverDestinatarioInexistente() {
		assertThrows(EntityNotFoundException.class, () -> {
			service.removeDestinatario(171L);
		});
	}
	
	@Test
	public void tentaAtualizarDestinatarioInexistente() {
		assertThrows(EntityNotFoundException.class, () -> {
			service.salvarDestinatario(new DestinatarioTO(), 171L);
		});
	}


}
