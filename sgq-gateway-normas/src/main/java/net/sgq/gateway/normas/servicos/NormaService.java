package net.sgq.gateway.normas.servicos;

import java.util.List;

import net.sgq.gateway.normas.modelos.Norma;

public interface NormaService {

	List<Norma> listaNormas();

	Norma consultaNorma(Long id);

}
