package net.sgq.transparencia.recall.modelos;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;

import net.sgq.transparencia.recall.modelos.enums.TipoRisco;

@Embeddable
public class InformacaoTecnica {

	@Column
	@PastOrPresent
	private Date dataConstatacao;
	
	@NotBlank
	@Column(length = 500, nullable = false)
	private String defeito;
	
	@Column
	@Enumerated(EnumType.STRING)
	private TipoRisco tipoRisco;

	public InformacaoTecnica() {
		super();
	}
	
	public InformacaoTecnica(Date dataConstatacao, String defeito, TipoRisco tipoRisco) {
		super();
		this.dataConstatacao = dataConstatacao;
		this.defeito = defeito;
		this.tipoRisco = tipoRisco;
	}

	public Date getDataConstatacao() {
		return dataConstatacao;
	}

	public void setDataConstatacao(Date dataConstatacao) {
		this.dataConstatacao = dataConstatacao;
	}

	public String getDefeito() {
		return defeito;
	}

	public void setDefeito(String defeito) {
		this.defeito = defeito;
	}

	public TipoRisco getTipoRisco() {
		return tipoRisco;
	}

	public void setTipoRisco(TipoRisco tipoRisco) {
		this.tipoRisco = tipoRisco;
	}
	
}
