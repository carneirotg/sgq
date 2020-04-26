package net.sgq.transparencia.recall.modelos.enums;

public enum Estado {

	ATIVA("Em Andamento"),
	CONCLUIDA("Concluída");

	private String estado;

	Estado(String estado) {
		this.estado = estado;
	}

	public String getEstado() {
		return estado;
	}
	
}
