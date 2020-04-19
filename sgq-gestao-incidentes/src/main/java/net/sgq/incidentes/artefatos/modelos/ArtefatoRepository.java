package net.sgq.incidentes.artefatos.modelos;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtefatoRepository extends JpaRepository<Artefato, Long> {

	List<Artefato> findByNomeContaining(String nome, Pageable page);
	
	@Modifying
	@Query("update Artefato a set a.depreciado = true where a.id = :id")
	int setDepreciado(Long id);
	
}
