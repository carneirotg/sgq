package net.sgq.incidentes.incidentes.modelos.to;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import net.sgq.incidentes.conformidades.modelos.enums.Setor;
import net.sgq.incidentes.incidentes.modelos.enums.ClassificacaoIncidente;
import net.sgq.incidentes.incidentes.modelos.enums.TipoIncidente;

public class IncidenteTO {

	@NotBlank
	@Size(max = 100)
	private String titulo;

	@NotBlank
	@Size(max = 100)
	private String descricao;

	@NotBlank
	@Size(max = 2000)
	private String conclusao;

	@NotNull
	private Setor setor;

	@NotNull
	private ClassificacaoIncidente classificacao;

	@NotNull
	private TipoIncidente tipoIncidente;

	public IncidenteTO(String titulo, String descricao, String conclusao, Setor setor,
			ClassificacaoIncidente classificacao, TipoIncidente tipoIncidente) {
		super();
		this.titulo = titulo;
		this.descricao = descricao;
		this.conclusao = conclusao;
		this.setor = setor;
		this.classificacao = classificacao;
		this.tipoIncidente = tipoIncidente;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getConclusao() {
		return conclusao;
	}

	public void setConclusao(String conclusao) {
		this.conclusao = conclusao;
	}

	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	public ClassificacaoIncidente getClassificacao() {
		return classificacao;
	}

	public void setClassificacao(ClassificacaoIncidente classificacao) {
		this.classificacao = classificacao;
	}

	public TipoIncidente getTipoIncidente() {
		return tipoIncidente;
	}

	public void setTipoIncidente(TipoIncidente tipoIncidente) {
		this.tipoIncidente = tipoIncidente;
	}
	

}
