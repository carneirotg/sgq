package net.sgq.incidentes.incidentes.modelos;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.sgq.incidentes.conformidades.modelos.enums.Estado;

@Repository
public interface IncidenteRepository extends JpaRepository<Incidente, Long> {

	Page<Incidente> findByTituloContaining(String nome, Pageable pageable);

	Page<Incidente> findBySituacao(Estado estado, Pageable pageable);
	Page<Incidente> findBySituacaoAndConcluidoEmAfter(Estado estado, Date criadoEm, Pageable pageable);
	Page<Incidente> findBySituacaoNot(Estado concluida, Pageable pageable);
	
	List<Incidente> findByCriadoEmBetween(Date inicio, Date fim);
	

}
