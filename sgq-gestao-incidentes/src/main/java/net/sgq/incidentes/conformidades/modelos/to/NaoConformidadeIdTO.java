package net.sgq.incidentes.conformidades.modelos.to;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import net.sgq.incidentes.conformidades.modelos.Norma;
import net.sgq.incidentes.conformidades.modelos.enums.Estado;
import net.sgq.incidentes.conformidades.modelos.enums.Setor;
import net.sgq.incidentes.conformidades.modelos.enums.TipoNaoConformidade;

public class NaoConformidadeIdTO extends NaoConformidadeTO {

	@NotNull
	@Positive
	private Long id;
	
	@NotNull
	private Estado estado;
	
	private Norma norma;

	public NaoConformidadeIdTO(String titulo, TipoNaoConformidade tipoNaoConformidade, String resumo,
			String detalhamentoNaoConformidade, Setor setor, Long artefato, String detalhamentoArtefato,
			Boolean prejuizoApurado, Long id, Estado estado, Norma norma) {
		super(titulo, tipoNaoConformidade, resumo, detalhamentoNaoConformidade, setor, artefato, detalhamentoArtefato,
				prejuizoApurado);
		this.id = id;
		this.estado = estado;
		this.norma = norma;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Norma getNorma() {
		return norma;
	}

	public void setNorma(Norma norma) {
		this.norma = norma;
	}

}
