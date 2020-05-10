package net.sgq.incidentes.conformidades.modelos.enums;

public enum TipoNaoConformidade {

	AUSENCIA_MEDIDA("Ausência/Incorreção de Medição"),
	ESPECIFICACAO_DIVERGENTE("Especificação Divergente"),
	QUANTIDADE_DIVERGENTE("Quantidade Divergente"),
	INSTRUCAO_DIVERGENTE("Instrução Divergente"),
	ANOMALIA("Anomalia Diversa");

	private String tipo;

	TipoNaoConformidade(String tipo) {
		this.tipo = tipo;
	}
	
	public String getTipo() {
		return this.tipo;
	}
	
}
