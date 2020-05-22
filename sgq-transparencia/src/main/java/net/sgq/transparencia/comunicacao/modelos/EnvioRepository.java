package net.sgq.transparencia.comunicacao.modelos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnvioRepository extends JpaRepository<Envio, Long> {

	Boolean existsByComunicadoIdAndComunicadoTipoAndDestinatarioId(Long comunicadoId, String comunicadoTipo, Long destinatarioId);
	
}
