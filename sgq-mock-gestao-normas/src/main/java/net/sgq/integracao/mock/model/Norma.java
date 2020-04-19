package net.sgq.integracao.mock.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "normas")
public class Norma {

	@Id
	private Long id;
	
	@Column(length = 100)
	private String nome;
	
	@Column
	@Enumerated(EnumType.STRING)
	private AreaIndustrial areaIndustrial;
	
	@Column(length = 500)
	private String resumo;
	
	@Column(length = 500)
	private String linkRepositorio;
	
	@ManyToMany
	private List<CheckListItem> checkList;

	public Norma() {
		super();
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

	public AreaIndustrial getAreaIndustrial() {
		return areaIndustrial;
	}

	public void setAreaIndustrial(AreaIndustrial areaIndustrial) {
		this.areaIndustrial = areaIndustrial;
	}

	public String getResumo() {
		return resumo;
	}

	public void setResumo(String resumo) {
		this.resumo = resumo;
	}

	public String getLinkRepositorio() {
		return linkRepositorio;
	}

	public void setLinkRepositorio(String linkRepositorio) {
		this.linkRepositorio = linkRepositorio;
	}
	
	public List<CheckListItem> getCheckList() {
		return checkList;
	}

	public void setCheckList(List<CheckListItem> checkList) {
		this.checkList = checkList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((areaIndustrial == null) ? 0 : areaIndustrial.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((linkRepositorio == null) ? 0 : linkRepositorio.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((resumo == null) ? 0 : resumo.hashCode());
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
		Norma other = (Norma) obj;
		if (areaIndustrial != other.areaIndustrial)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (linkRepositorio == null) {
			if (other.linkRepositorio != null)
				return false;
		} else if (!linkRepositorio.equals(other.linkRepositorio))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (resumo == null) {
			if (other.resumo != null)
				return false;
		} else if (!resumo.equals(other.resumo))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Norma [id=" + id + ", nome=" + nome + "]";
	}
	
}
