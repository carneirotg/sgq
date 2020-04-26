package net.sgq.transparencia.recall.modelos;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.sgq.transparencia.recall.modelos.enums.Estado;

@Repository
public interface CampanhaRecallRepository extends JpaRepository<CampanhaRecall, Long> {

	List<CampanhaRecall> findByEstadoCampanha(Estado estado);
	List<CampanhaRecall> findByEstadoCampanhaAndInicioAfter(Estado estado, Date date);

}
