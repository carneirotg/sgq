package net.sgq.transparencia.recall.modelos;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.sgq.transparencia.recall.modelos.enums.Estado;

@Repository
public interface CampanhaRecallRepository extends JpaRepository<CampanhaRecall, Long> {

	List<CampanhaRecall> findByEstadoCampanha(Estado estado);
	Page<CampanhaRecall> findByEstadoCampanha(Estado estado, Pageable page);
	List<CampanhaRecall> findByEstadoCampanhaAndInicioAfter(Estado estado, Date date);
	
	@Query("select c from CampanhaRecall as c where c.estadoCampanha = :estado and (lower(c.titulo) like lower(:termos) or lower(c.informativoCampanha) like lower(:termos) or lower(c.medidasCorretivas) like lower(:termos) or lower(c.informacaoTecnica.defeito) like lower(:termos))")
	Page<CampanhaRecall> findByEstadoCampanhaAndTermos(@Param("estado") Estado estado, @Param("termos") String termos, Pageable page);

}
