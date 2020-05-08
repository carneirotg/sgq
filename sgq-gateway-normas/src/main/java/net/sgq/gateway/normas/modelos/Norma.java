package net.sgq.gateway.normas.modelos;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Norma {

	private Long normaId;
	
	@JsonIgnore
	private String tituloNorma;
	
	private String area;
	
	private String resumo;

	private Map<String, Boolean> checkList = new LinkedHashMap<>();
	
	private String linkRepositorio;

	public Norma() {
		super();
	}
	
	public Norma(Long normaId, String norma, Map<String, Boolean> checkList) {
		super();
		this.normaId = normaId;
		this.tituloNorma = norma;
		this.checkList = checkList;
	}

	public Norma(Map<String, Object> normaResponse) {
		this.setNormaId(((Integer) normaResponse.get("id")).longValue());
		this.setNorma((String) normaResponse.get("nome"));
		this.setArea((String) normaResponse.get("areaIndustrial"));
		this.setLinkRepositorio((String) normaResponse.get("linkRepositorio"));
		this.setResumo((String) normaResponse.get("resumo"));
		
		@SuppressWarnings("unchecked")
		List<Object> normaCheckList = (List<Object>) normaResponse.get("checkList");
		
		for(Object item : normaCheckList) {
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
		return tituloNorma;
	}

	public void setNorma(String norma) {
		this.tituloNorma = norma;
	}

	public Map<String, Boolean> getCheckList() {
		return checkList;
	}

	public void setCheckList(Map<String, Boolean> checkList) {
		this.checkList = checkList;
	}

	public String getTituloNorma() {
		return tituloNorma;
	}

	public void setTituloNorma(String tituloNorma) {
		this.tituloNorma = tituloNorma;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
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

}
