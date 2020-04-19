package net.sgq.integracao.mock.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NormaRepository extends JpaRepository<Norma, Long> {

}
