package net.sgq.transparencia.recall.modelos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;

import net.sgq.transparencia.recall.modelos.enums.Estado;
import net.sgq.transparencia.recall.modelos.to.CampanhaRecallTO;

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
	@Type(type = "org.hibernate.type.TextType") 
	private String informativoCampanha;

	@NotBlank
	@Column(length = 1000, nullable = false)
	private String medidasCorretivas;

	@Column(nullable = false)
	private Boolean incidentesConhecidos;

	@Embedded
	private InformacaoTecnica informacaoTecnica;

	@ManyToMany(fetch = FetchType.EAGER)
	@Size(min = 1)
	private List<NaoConformidade> ncsEnvolvidas = new ArrayList<>();

	@Enumerated(EnumType.STRING)
	@Column
	private Estado estadoCampanha;

	public CampanhaRecall() {
		super();
	}

	@PrePersist
	public void prePersist() {
		this.inicio = new Date();
		this.estadoCampanha = Estado.ATIVA;
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

	public CampanhaRecallTO toTO() {
		return new CampanhaRecallTO(getId(), getTitulo(), getInicio(), getFim(), getInformativoCampanha(),
				getMedidasCorretivas(), getIncidentesConhecidos(), getInformacaoTecnica().getDataConstatacao(),
				getInformacaoTecnica().getDefeito(), getInformacaoTecnica().getTipoRisco(), getNcsEnvolvidas(),
				getEstadoCampanha());
	}
	
	public CampanhaRecall fromTO(CampanhaRecallTO to) {
		setEstadoCampanha(to.getEstadoCampanha());
		setIncidentesConhecidos(to.getIncidentesConhecidos());
		setInformacaoTecnica(new InformacaoTecnica(to.getDataConstatacao(), to.getDefeito(), to.getTipoRisco()));
		setInformativoCampanha(to.getInformativoCampanha());
		setMedidasCorretivas(to.getMedidasCorretivas());
		setNcsEnvolvidas(to.getNcsEnvolvidas());
		setTitulo(to.getTitulo());
		setFim(to.getFim());
		
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	@SuppressWarnings({"squid:S3776"})
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		CampanhaRecall other = (CampanhaRecall) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

}
