package net.sgq.incidentes.utils;

public class EntityNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 5009824394959108397L;

	public EntityNotFoundException(String classe, Long id) {
		super(String.format("Entidade do tipo '%s' não encontrada para Id %d", classe, id));
	}

}
