package net.sgq.transparencia.recall.modelos.enums;

public enum TipoRisco {

	FOGO("Fogo / Incêndio / Queimadura", "Incêndio provocado por faísca ou superaquecimento.", "Danos físicos ou materiais"),
	LESAO("Lesão / Laceração", "Objeto indevidamente cortante, mal acabado ou passível de desprender e provocar ferimentos.", "Danos físicos"),
	QUEDA("Queda", "Objeto mal afixado e/ou pouco resistente", "Danos físicos ou materiais"),
	VARIOS("Vários", "Diversos tipos de riscos foram apontados.", "Danos fíciso e/ou materiais");
	

	private String tipo;
	private String descricao;
	private String implicacoes;

	TipoRisco(String tipo, String descricao, String implicacoes) {
		this.tipo = tipo;
		this.descricao = descricao;
		this.implicacoes = implicacoes;
	}
	
	public String getTipo() {
		return this.tipo;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getImplicacoes() {
		return implicacoes;
	}
	
}
