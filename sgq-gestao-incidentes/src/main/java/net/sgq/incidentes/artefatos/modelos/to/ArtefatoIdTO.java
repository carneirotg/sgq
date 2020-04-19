package net.sgq.incidentes.artefatos.modelos.to;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class ArtefatoIdTO extends ArtefatoTO {

	@NotNull
	@Positive
	private Long id;

	public ArtefatoIdTO() {
		super();
	}

	public ArtefatoIdTO(String nome, String descricao,
			Date criadoEm, String urlImagem, Long id, Boolean depreciado) {
		super(nome, descricao, criadoEm, urlImagem, depreciado);
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
