package net.sgq.incidentes.conformidades.modelos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.sgq.incidentes.conformidades.modelos.enums.Estado;

@Repository
public interface NaoConformidadeRepository extends JpaRepository<NaoConformidade, Long> {

	Page<NaoConformidade> findByTituloContaining(String titulo, Pageable pageable);

	Page<NaoConformidade> findByEstado(Estado estado, Pageable pageable);
	
	Page<NaoConformidade> findByEstadoNot(Estado estado, Pageable pageable);

}
