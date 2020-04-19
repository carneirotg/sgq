package net.sgq.incidentes.artefatos.modelos.to;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ArtefatoTO {

	@Size(min = 3)
	private String nome;

	@NotBlank
	@Size(max = 500)
	private String descricao;
	
	private Date criadoEm;

	@Size(max = 500)
	private String urlImagem;
	
	private Boolean depreciado;

	public ArtefatoTO() {
		super();
	}

	public ArtefatoTO(String nome, String descricao, Date criadoEm, String urlImagem, Boolean depreciado) {
		super();
		this.nome = nome;
		this.descricao = descricao;
		this.criadoEm = criadoEm;
		this.urlImagem = urlImagem;
		this.depreciado = depreciado;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getCriadoEm() {
		return criadoEm;
	}

	public void setCriadoEm(Date criadoEm) {
		this.criadoEm = criadoEm;
	}

	public String getUrlImagem() {
		return urlImagem;
	}

	public void setUrlImagem(String urlImagem) {
		this.urlImagem = urlImagem;
	}

	public Boolean getDepreciado() {
		return depreciado;
	}

	public void setDepreciado(Boolean depreciado) {
		this.depreciado = depreciado;
	}

}
