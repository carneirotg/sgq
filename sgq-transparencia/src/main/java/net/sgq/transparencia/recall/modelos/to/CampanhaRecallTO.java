package net.sgq.transparencia.recall.modelos.to;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

import net.sgq.transparencia.recall.modelos.NaoConformidade;
import net.sgq.transparencia.recall.modelos.enums.Estado;
import net.sgq.transparencia.recall.modelos.enums.TipoRisco;

public class CampanhaRecallTO {

	private Long id;

	@NotBlank
	private String titulo;

	@PastOrPresent
	private Date inicio;

	@Future
	private Date fim;

	@NotBlank
	private String informativoCampanha;

	@NotBlank
	private String medidasCorretivas;

	@NotNull
	private Boolean incidentesConhecidos;

	private Date dataConstatacao;
	
	@NotBlank
	private String defeito;
	
	@Column
	private TipoRisco tipoRisco;

	@Size(min = 1)
	private List<NaoConformidade> ncsEnvolvidas = new ArrayList<>();

	private Estado estadoCampanha;

	public CampanhaRecallTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CampanhaRecallTO(Long id, String titulo, Date inicio, Date fim, String informativoCampanha,
			String medidasCorretivas, Boolean incidentesConhecidos, Date dataConstatacao, String defeito,
			TipoRisco tipoRisco, List<NaoConformidade> ncsEnvolvidas, Estado estadoCampanha) {
		super();
		this.id = id;
		this.titulo = titulo;
		this.inicio = inicio;
		this.fim = fim;
		this.informativoCampanha = informativoCampanha;
		this.medidasCorretivas = medidasCorretivas;
		this.incidentesConhecidos = incidentesConhecidos;
		this.dataConstatacao = dataConstatacao;
		this.defeito = defeito;
		this.tipoRisco = tipoRisco;
		this.ncsEnvolvidas = ncsEnvolvidas;
		this.estadoCampanha = estadoCampanha;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Date getInicio() {
		return inicio;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	public Date getFim() {
		return fim;
	}

	public void setFim(Date fim) {
		this.fim = fim;
	}

	public String getInformativoCampanha() {
		return informativoCampanha;
	}

	public void setInformativoCampanha(String informativoCampanha) {
		this.informativoCampanha = informativoCampanha;
	}

	public String getMedidasCorretivas() {
		return medidasCorretivas;
	}

	public void setMedidasCorretivas(String medidasCorretivas) {
		this.medidasCorretivas = medidasCorretivas;
	}

	public Boolean getIncidentesConhecidos() {
		return incidentesConhecidos;
	}

	public void setIncidentesConhecidos(Boolean incidentesConhecidos) {
		this.incidentesConhecidos = incidentesConhecidos;
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

	public List<NaoConformidade> getNcsEnvolvidas() {
		return ncsEnvolvidas;
	}

	public void setNcsEnvolvidas(List<NaoConformidade> ncsEnvolvidas) {
		this.ncsEnvolvidas = ncsEnvolvidas;
	}

	public Estado getEstadoCampanha() {
		return estadoCampanha;
	}

	public void setEstadoCampanha(Estado estadoCampanha) {
		this.estadoCampanha = estadoCampanha;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CampanhaRecallTO other = (CampanhaRecallTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
