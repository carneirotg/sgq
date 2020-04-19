package net.sgq.incidentes.conformidades.modelos.to;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import net.sgq.incidentes.conformidades.modelos.enums.Setor;
import net.sgq.incidentes.conformidades.modelos.enums.TipoNaoConformidade;

public class NaoConformidadeTO {

	@NotBlank
	private String titulo;

	@NotNull
	private TipoNaoConformidade tipoNaoConformidade;

	@NotBlank
	@Size(max = 300)
	private String resumo;

	@NotBlank
	private String detalhamentoNaoConformidade;

	@NotNull
	private Setor setor;

	@Positive
	@NotNull
	private Long artefato;

	@NotBlank
	@Size(max = 200)
	private String detalhamentoArtefato;

	@NotNull
	private Boolean prejuizoApurado;

	public NaoConformidadeTO() {
		super();
	}

	public NaoConformidadeTO(String titulo, TipoNaoConformidade tipoNaoConformidade, String resumo,
			String detalhamentoNaoConformidade, Setor setor, Long artefato, String detalhamentoArtefato,
			Boolean prejuizoApurado) {
		super();
		this.titulo = titulo;
		this.tipoNaoConformidade = tipoNaoConformidade;
		this.resumo = resumo;
		this.detalhamentoNaoConformidade = detalhamentoNaoConformidade;
		this.setor = setor;
		this.artefato = artefato;
		this.detalhamentoArtefato = detalhamentoArtefato;
		this.prejuizoApurado = prejuizoApurado;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public TipoNaoConformidade getTipoNaoConformidade() {
		return tipoNaoConformidade;
	}

	public void setTipoNaoConformidade(TipoNaoConformidade tipoNaoConformidade) {
		this.tipoNaoConformidade = tipoNaoConformidade;
	}

	public String getResumo() {
		return resumo;
	}

	public void setResumo(String resumo) {
		this.resumo = resumo;
	}

	public String getDetalhamentoNaoConformidade() {
		return detalhamentoNaoConformidade;
	}

	public void setDetalhamentoNaoConformidade(String detalhamentoNaoConformidade) {
		this.detalhamentoNaoConformidade = detalhamentoNaoConformidade;
	}

	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	public Long getArtefato() {
		return artefato;
	}

	public void setArtefato(Long artefato) {
		this.artefato = artefato;
	}

	public String getDetalhamentoArtefato() {
		return detalhamentoArtefato;
	}

	public void setDetalhamentoArtefato(String detalhamentoArtefato) {
		this.detalhamentoArtefato = detalhamentoArtefato;
	}

	public Boolean getPrejuizoApurado() {
		return prejuizoApurado;
	}

	public void setPrejuizoApurado(Boolean prejuizoApurado) {
		this.prejuizoApurado = prejuizoApurado;
	}

}
