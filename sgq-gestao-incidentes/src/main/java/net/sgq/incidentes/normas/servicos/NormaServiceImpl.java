package net.sgq.incidentes.normas.servicos;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sgq.incidentes.conformidades.modelos.Norma;
import net.sgq.incidentes.integracoes.clientes.GestaoNormaClient;
import net.sgq.incidentes.utils.EntityNotFoundException;

@Service
public class NormaServiceImpl implements NormaService {

	private Logger logger = LoggerFactory.getLogger(NormaServiceImpl.class);
	
	@Autowired
	private GestaoNormaClient client;
	
	@Override
	public List<Norma> listaNormas() {
		
		try {
			return client.consultaListaNormas();
		} catch(Exception e) {
			logger.error("Erro integração GN: {}", e.getMessage());
			throw new EntityNotFoundException("[GN]Norma", 0L);
		}
		
	}

	@Override
	public Norma consultaNorma(Long id) {

		try {
			return client.consultaNorma(id);
		} catch(Exception e) {
			logger.error("Erro integração GN: {}", e.getMessage());
			throw new EntityNotFoundException("[GN]Norma", id);
		}
	}

}
