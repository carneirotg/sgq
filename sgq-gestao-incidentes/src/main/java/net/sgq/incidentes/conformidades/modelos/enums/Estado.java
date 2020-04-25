package net.sgq.incidentes.conformidades.modelos.enums;

public enum Estado {

	ABERTA("Aberta"),
	EM_ANALISE("Em Análise"),
	CONCLUIDA("Concluída"),
	NAO_CONCLUIDA("Não Concluida");

	private String nome;

	Estado(String nome) {
		this.nome = nome;
	}
	
	public String getNome() {
		return this.nome;
	}
	
	public static Estado getEnum(String nome) {
		switch ((nome == null) ? "nulo" : nome) {
		case "aberta":
			return ABERTA;
		case "em análise":
			return EM_ANALISE;
		case "concluída":
			return CONCLUIDA;
		case "não concluída":
			return CONCLUIDA;
		default:
			throw new IllegalArgumentException("Enumeração invalida: " + nome);
		}
	}
	
}
