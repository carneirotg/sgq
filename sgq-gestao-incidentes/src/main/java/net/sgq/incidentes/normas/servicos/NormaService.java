package net.sgq.incidentes.normas.servicos;

import java.util.List;

import net.sgq.incidentes.conformidades.modelos.Norma;

public interface NormaService {

	List<Norma> listaNormas();

	Norma consultaNorma(Long id);

}
