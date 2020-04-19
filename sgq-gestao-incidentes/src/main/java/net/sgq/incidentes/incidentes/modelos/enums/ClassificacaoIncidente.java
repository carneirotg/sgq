package net.sgq.incidentes.incidentes.modelos.enums;

public enum ClassificacaoIncidente {
	
	BAIXO("Baixo"),
	MEDIO("Médio"),
	ALTO("Alto"),
	CRITICO("Crítico");

	private String classif;

	ClassificacaoIncidente(String classif) {
		this.classif = classif;
	}
	
	public String getClassificacao() {
		return this.classif;
	}

}
