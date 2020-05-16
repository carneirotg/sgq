package net.sgq.incidentes.incidentes.modelos;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.sgq.incidentes.conformidades.modelos.enums.Estado;

@Repository
public interface IncidenteRepository extends JpaRepository<Incidente, Long> {

	Page<Incidente> findByTituloContaining(String nome, Pageable pageable);

	Page<Incidente> findBySituacao(Estado estado, Pageable pageable);
	@Query("select i from Incidente as i where i.situacao = :estado and (lower(i.titulo) like lower(:termo) or lower(i.descricao) like lower(:termo) or lower(i.conclusao) like lower(:termo))")
	Page<Incidente> findBySituacaoAndTermo(Estado estado, String termo, Pageable pageable);
	
	Page<Incidente> findBySituacaoAndConcluidoEmAfter(Estado estado, Date criadoEm, Pageable pageable);
	
	Page<Incidente> findBySituacaoNot(Estado concluida, Pageable pageable);
	@Query("select i from Incidente as i where i.situacao <> :estado and (lower(i.titulo) like lower(:termo) or lower(i.descricao) like lower(:termo) or lower(i.conclusao) like lower(:termo))")
	Page<Incidente> findBySituacaoNotAndTermo(Estado estado, String termo, Pageable pageable);
	
	List<Incidente> findByCriadoEmBetween(Date inicio, Date fim);
	

}
