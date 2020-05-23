package net.sgq.incidentes.incidentes.modelos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

import net.sgq.incidentes.conformidades.modelos.NaoConformidade;
import net.sgq.incidentes.conformidades.modelos.enums.Estado;
import net.sgq.incidentes.conformidades.modelos.enums.Setor;
import net.sgq.incidentes.incidentes.modelos.enums.ClassificacaoIncidente;
import net.sgq.incidentes.incidentes.modelos.enums.TipoIncidente;

@Entity
@Table(name = "incidentes")
public class Incidente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Column(nullable = false, length = 100)
	private String titulo;

	@NotBlank
	@Column(nullable = false, length = 500)
	private String descricao;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date criadoEm;

	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date concluidoEm;

	@Lob
	@Column(nullable = false)
	@Type(type = "org.hibernate.type.TextType") 
	private String conclusao;

	@NotNull
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private ClassificacaoIncidente classificacao;

	@NotNull
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private TipoIncidente tipoIncidente;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Estado situacao;

	@NotNull
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Setor setor;

	@ManyToMany
	private List<NaoConformidade> ncEnvolvidas = new ArrayList<>();

	@PrePersist
	private void prePersist() {
		this.criadoEm = new Date();
		this.conclusao = "";
		this.situacao = Estado.ABERTA;
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

	public Date getConcluidoEm() {
		return concluidoEm;
	}

	public void setConcluidoEm(Date concluidoEm) {
		this.concluidoEm = concluidoEm;
	}

	public String getConclusao() {
		return conclusao;
	}

	public void setConclusao(String conclusao) {
		this.conclusao = conclusao;
	}

	public ClassificacaoIncidente getClassificacao() {
		return classificacao;
	}

	public void setClassificacao(ClassificacaoIncidente classificao) {
		this.classificacao = classificao;
	}

	public TipoIncidente getTipoIncidente() {
		return tipoIncidente;
	}

	public void setTipoIncidente(TipoIncidente tipoIncidente) {
		this.tipoIncidente = tipoIncidente;
	}

	public Estado getSituacao() {
		return situacao;
	}

	public void setSituacao(Estado situacao) {
		this.situacao = situacao;
	}

	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	public List<NaoConformidade> getNcEnvolvidas() {
		return ncEnvolvidas;
	}

	public void setNcEnvolvidas(List<NaoConformidade> ncEnvolvidas) {
		this.ncEnvolvidas = ncEnvolvidas;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((classificacao == null) ? 0 : classificacao.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((setor == null) ? 0 : setor.hashCode());
		result = prime * result + ((situacao == null) ? 0 : situacao.hashCode());
		result = prime * result + ((tipoIncidente == null) ? 0 : tipoIncidente.hashCode());
		return result;
	}

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
		Incidente other = (Incidente) obj;
		if (classificacao != other.classificacao) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (setor != other.setor) {
			return false;
		}
		if (situacao != other.situacao) {
			return false;
		}
		if (tipoIncidente != other.tipoIncidente) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Incidente [id=" + id + ", titulo=" + titulo + ", tipoIncidente=" + tipoIncidente + "]";
	}

}
