package net.sgq.incidentes.conformidades.modelos.enums;

public enum Setor {
	COMPRAS("Compras"),
	LINHA("Linha de Montagem"),
	LOGISTICA("Logística"),
	INSPECAO("Inspeção");

	private String nome;

	Setor(String nome) {
		this.nome = nome;
	}
	
	public String getNome() {
		return this.nome;
	}
}
