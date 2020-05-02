package net.sgq.gateway.authentication.usuarios.modelos.enums;

public enum Papeis {
	GESTOR,
	ANALISTA;
	
	public String getAuthority() {
		return String.format("ROLE_%s", this.name());
	}
	
}
