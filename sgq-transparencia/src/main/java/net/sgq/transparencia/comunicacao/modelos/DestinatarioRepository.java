package net.sgq.transparencia.comunicacao.modelos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DestinatarioRepository extends JpaRepository<Destinatario, Long> {

	List<Destinatario> findByAssinanteRecallIsTrue();
	List<Destinatario> findByAssinanteEventosIsTrue();
	
}
