package net.sgq.incidentes.conformidades.modelos;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.validation.constraints.Positive;

@Embeddable
public class Norma {

	@Positive
	@Column
	private Long normaId;
	
	@Column(length = 100)
	private String norma;

	@ElementCollection
	@CollectionTable(name = "check_list_instancia", 
	joinColumns = {
			@JoinColumn(name = "nc_id", referencedColumnName = "id") 
	})
	@MapKeyColumn(name = "criterio_checklist")
	@Column(name = "estado_criterio_checklist")
	private Map<String, Boolean> checkList = new LinkedHashMap<>();

	public Norma() {
		super();
	}
	
	public Norma(@Positive Long normaId, String norma, Map<String, Boolean> checkList) {
		super();
		this.normaId = normaId;
		this.norma = norma;
		this.checkList = checkList;
	}

	public Norma(Map<String, Object> normaResponse) {
		this.setNormaId(((Integer) normaResponse.get("id")).longValue());
		this.setNorma((String) normaResponse.get("nome"));
		
		@SuppressWarnings("unchecked")
		List<Object> checkList = (List<Object>) normaResponse.get("checkList");
		
		for(Object item : checkList) {
			@SuppressWarnings("unchecked")
			Map<String, String> entry = (Map<String, String>) item;
			
			this.checkList.put(entry.get("criterio"), Boolean.FALSE);
		}
		
	}

	public Long getNormaId() {
		return normaId;
	}

	public void setNormaId(Long normaId) {
		this.normaId = normaId;
	}

	public String getNorma() {
		return norma;
	}

	public void setNorma(String norma) {
		this.norma = norma;
	}

	public Map<String, Boolean> getCheckList() {
		return checkList;
	}

	public void setCheckList(Map<String, Boolean> checkList) {
		this.checkList = checkList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((normaId == null) ? 0 : normaId.hashCode());
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
		Norma other = (Norma) obj;
		if (normaId == null) {
			if (other.normaId != null) {
				return false;
			}
		} else if (!normaId.equals(other.normaId)) {
			return false;
		}
		return true;
	}
	
}
