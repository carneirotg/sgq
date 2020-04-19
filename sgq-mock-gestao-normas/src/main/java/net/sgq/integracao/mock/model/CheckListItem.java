package net.sgq.integracao.mock.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "checklist_items")
public class CheckListItem {

	@Id
	private Long id;
	
	@Column(length = 100)
	private String criterio;
	
	@ManyToMany(mappedBy = "checkList")
	@JsonIgnore
	private List<Norma> normas = new ArrayList<>();

	public CheckListItem() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCriterio() {
		return criterio;
	}

	public void setCriterio(String criterio) {
		this.criterio = criterio;
	}

	public List<Norma> getNormas() {
		return normas;
	}

	public void setNormas(Norma norma) {
		this.normas.add(norma);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((criterio == null) ? 0 : criterio.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((normas == null) ? 0 : normas.hashCode());
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
		CheckListItem other = (CheckListItem) obj;
		if (criterio == null) {
			if (other.criterio != null)
				return false;
		} else if (!criterio.equals(other.criterio))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (normas == null) {
			if (other.normas != null)
				return false;
		} else if (!normas.equals(other.normas))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CheckListItem [id=" + id + ", criterio=" + criterio + ", normas=" + normas.size() + "]";
	}
	
	
	
}
