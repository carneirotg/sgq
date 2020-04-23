package net.sgq.transparencia.clientes.to;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement
public class Incidentes {

	@JacksonXmlProperty(localName = "Incidente")
	@JacksonXmlElementWrapper(useWrapping = false)
	private List<IncidenteTO> incidentes;

	public Incidentes(List<IncidenteTO> incidentes) {
		this.incidentes = incidentes;
	}

	public List<IncidenteTO> getIncidentes() {
		return incidentes;
	}

	public void setIncidentes(List<IncidenteTO> incidentes) {
		this.incidentes = incidentes;
	}
	
}
