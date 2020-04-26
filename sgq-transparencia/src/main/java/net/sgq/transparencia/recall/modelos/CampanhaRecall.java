package net.sgq.transparencia.recall.modelos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;

@Entity
@Table(name = "campanhas_recall")
public class CampanhaRecall {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Column(length = 100, nullable = false)
	private String titulo;
	
	@PastOrPresent
	@Column(nullable = false)
	private Date inicio;
	
	@Future
	@Column(nullable = false)
	private Date fim;
	
	@NotBlank
	@Lob
	private String informativoCampanha;
	
	@NotBlank
	@Column(length = 1000, nullable = false)
	private String medidasCorretivas;
	
	@Column(nullable = false)
	private Boolean incidentesConhecidos;
	
	@Embedded
	private InformacaoTecnica informacaoTecnica;
	
	@ElementCollection
	@CollectionTable(name = "recalls_artefatos_envolvidos",
	joinColumns = {
			@JoinColumn(referencedColumnName = "id", name = "recall_id")
	})
	@Column(name = "artefatos_envolvidos")
	private List<Long> artefatosEnvolvidos = new ArrayList<>();

	public CampanhaRecall() {
		super();
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

	public InformacaoTecnica getInformacaoTecnica() {
		return informacaoTecnica;
	}

	public void setInformacaoTecnica(InformacaoTecnica informacaoTecnica) {
		this.informacaoTecnica = informacaoTecnica;
	}

	public List<Long> getArtefatosEnvolvidos() {
		return artefatosEnvolvidos;
	}

	public void setArtefatosEnvolvidos(List<Long> artefatosEnvolvidos) {
		this.artefatosEnvolvidos = artefatosEnvolvidos;
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
		CampanhaRecall other = (CampanhaRecall) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
