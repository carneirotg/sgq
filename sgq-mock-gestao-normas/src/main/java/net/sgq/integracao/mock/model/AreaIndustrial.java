package net.sgq.integracao.mock.model;

public enum AreaIndustrial {

	QUALIDADE("Qualidade"),
	TI("Tecnologia da Informação"),
	AUTOMOVEL("Automóvel"),
	AMBIENTE("Ambiental"),
	SEGURANCA_TRABALHO("Segurança no Trabalho"),
	SEGURANCA_ALIMENTAR("Segurança Alimentar");

	private String area;

	AreaIndustrial(String area) {
		this.area = area;
	}
	
	public String getArea() {
		return this.area;
	}
	
}
