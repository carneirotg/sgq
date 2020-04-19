package net.sgq.incidentes.incidentes.modelos.enums;

public enum TipoIncidente {

	SEM_DANO("Sem dano"),
	PARADA("Parada de produção"),
	DANO_FINANCEIRO("Dano financeiro"),
	DANO_PESSOA("Dano a pessoa");

	private String nome;

	TipoIncidente(String nome) {
		this.nome = nome;
	}
	
	public String getNome() {
		return this.nome;
	}
	
}
