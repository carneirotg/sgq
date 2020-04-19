package net.sgq.incidentes.incidentes.modelos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.sgq.incidentes.conformidades.modelos.enums.Estado;

@Repository
public interface IncidenteRepository extends JpaRepository<Incidente, Long> {

	List<Incidente> findByTituloContaining(String nome);

	Optional<Incidente> findBySituacao(Estado estado);

}
