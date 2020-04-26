package net.sgq.incidentes.artefatos.modelos;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import net.sgq.incidentes.artefatos.modelos.to.ArtefatoIdTO;
import net.sgq.incidentes.artefatos.modelos.to.ArtefatoTO;
import net.sgq.incidentes.utils.TOAble;

@Entity
@Table(name = "artefatos")
public class Artefato implements TOAble<Artefato, ArtefatoTO, ArtefatoIdTO> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Size(min = 3)
	@Column(length = 75, nullable = false)
	private String nome;
	
	@NotBlank
	@Column(length = 500, nullable = false)
	private String descricao;
	
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date criadoEm;
	
	@Column(length = 500, nullable = true)
	private String urlImagem;
	
	@Column
	private Boolean depreciado;

	public Artefato() {
		super();
	}
	
	@PrePersist
	private void criadoEm() {
		this.criadoEm = new Date();
		this.depreciado = Boolean.FALSE;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((criadoEm == null) ? 0 : criadoEm.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
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
		Artefato other = (Artefato) obj;
		if (criadoEm == null) {
			if (other.criadoEm != null)
				return false;
		} else if (!criadoEm.equals(other.criadoEm))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}

	@Override
	public ArtefatoTO toTO() {
		return new ArtefatoTO(getNome(), getDescricao(), getCriadoEm(), getUrlImagem(), getDepreciado());
	}

	@Override
	public ArtefatoIdTO toTOId() {
		return new ArtefatoIdTO(getNome(), getDescricao(), getCriadoEm(), getUrlImagem(), getId(), getDepreciado());
	}

	@Override
	public Artefato fromTO(ArtefatoTO to) {
		setDescricao(to.getDescricao());
		setNome(to.getNome());
		setUrlImagem(to.getUrlImagem());
		
		return this;
	}

	@Override
	public Artefato fromTOID(ArtefatoIdTO toid) {
		fromTO(toid);
		setId(toid.getId());
		
		return this;
	}

	@Override
	public String toString() {
		return "Artefato [id=" + id + ", nome=" + nome + "]";
	}

}
