package net.sgq.gateway.normas.servicos;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import net.sgq.gateway.normas.cliente.GestaoNormaClient;
import net.sgq.gateway.normas.modelos.Norma;

@Service
public class NormaServiceImpl implements NormaService {

	private Logger logger = LoggerFactory.getLogger(NormaServiceImpl.class);
	
	@Autowired
	private GestaoNormaClient client;
	
	@Override
	@Cacheable(value = "normasCache", unless = "#result == null")
	public List<Norma> listaNormas() {
		
		try {
			return client.consultaListaNormas();
		} catch(Exception e) {
			logger.error("Erro integração GN: {}", e.getMessage());
			return null;
		}
		
	}

	@Override
	@Cacheable(value = "normasPorIDCache", unless = "#result == null")
	public Norma consultaNorma(Long id) {

		try {
			return client.consultaNorma(id);
		} catch(Exception e) {
			logger.error("Erro integração GN: {}", e.getMessage());
			return null;
		}
	}

}
