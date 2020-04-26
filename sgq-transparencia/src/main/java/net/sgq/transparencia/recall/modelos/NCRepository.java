package net.sgq.transparencia.recall.modelos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NCRepository extends JpaRepository<NaoConformidade, Long> {

}
