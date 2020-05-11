package net.sgq.incidentes.conformidades.modelos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.sgq.incidentes.conformidades.modelos.enums.Estado;

@Repository
public interface NaoConformidadeRepository extends JpaRepository<NaoConformidade, Long> {

	Page<NaoConformidade> findByTituloContaining(String titulo, Pageable pageable);

	Page<NaoConformidade> findByEstado(Estado estado, Pageable pageable);

	Page<NaoConformidade> findByEstadoNot(Estado estado, Pageable pageable);

	@Query("select nc from NaoConformidade as nc where nc.estado <> :estado and (lower(nc.titulo) like lower(:termo) or lower(nc.resumo) like lower(:termo) or lower(nc.detalhamentoNaoConformidade) like lower(:termo))")
	Page<NaoConformidade> findByEstadoNotAndTermo(@Param("estado") Estado estado, @Param("termo") String termo, Pageable pageable);

	@Query("select nc from NaoConformidade as nc where nc.estado =  :estado and (lower(nc.titulo) like lower(:termo) or lower(nc.resumo) like lower(:termo) or lower(nc.detalhamentoNaoConformidade) like lower(:termo))")
	Page<NaoConformidade> findByEstadoAndTermo(@Param("estado") Estado estado, @Param("termo") String termo, Pageable pageable);

}
