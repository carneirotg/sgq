package net.sgq.relatorios.modelos.enums;

public enum Periodo {
	
	MES_ATUAL("Mês Atual"),
	SEMESTRE("Semestre"),
	ANO_CORRENTE("Ano Corrente"),
	DOZE_MESES("Últimos Doze Meses");
	private String descricao;

	Periodo(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return this.descricao;
	}

}

