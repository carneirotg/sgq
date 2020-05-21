package net.sgq.transparencia.recall.modelos;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import net.sgq.transparencia.recall.modelos.to.CampanhaRecallTO;

@JacksonXmlRootElement
public class Campanhas {

	@JacksonXmlProperty(localName = "Campanha")
	@JacksonXmlElementWrapper(useWrapping = false)
	private List<CampanhaRecallTO> campanhas;

	public Campanhas(List<CampanhaRecallTO> campanhas) {
		this.campanhas = campanhas;
	}

	public List<CampanhaRecallTO> getCampanhas() {
		return campanhas;
	}

	public void setcampanhas(List<CampanhaRecallTO> campanhas) {
		this.campanhas = campanhas;
	}
	
}
