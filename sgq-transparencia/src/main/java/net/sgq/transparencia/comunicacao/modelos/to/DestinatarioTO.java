package net.sgq.transparencia.comunicacao.modelos.to;

import net.sgq.transparencia.comunicacao.modelos.Destinatario;
import net.sgq.transparencia.comunicacao.modelos.enums.TipoDestinatatio;

public class DestinatarioTO {

	private Long id;

	private String descricao;
	
	private String endpoint;
	
	private TipoDestinatatio tipoDestinatario;
	
	private Boolean assinanteRecall;
	
	private Boolean assinanteEventos;

	public DestinatarioTO() {
		super();
	}
	
	public DestinatarioTO(Destinatario dest) {
		super();
		setAssinanteEventos(dest.getAssinanteEventos());
		setAssinanteRecall(dest.getAssinanteRecall());
		setDescricao(dest.getDescricao());
		setEndpoint(dest.getEndpoint());
		setId(dest.getId());
		setTipoDestinatario(dest.getTipoDestinatario());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public TipoDestinatatio getTipoDestinatario() {
		return tipoDestinatario;
	}

	public void setTipoDestinatario(TipoDestinatatio tipoDestinatario) {
		this.tipoDestinatario = tipoDestinatario;
	}

	public Boolean getAssinanteRecall() {
		return assinanteRecall;
	}

	public void setAssinanteRecall(Boolean assinanteRecall) {
		this.assinanteRecall = assinanteRecall;
	}

	public Boolean getAssinanteEventos() {
		return assinanteEventos;
	}

	public void setAssinanteEventos(Boolean assinanteEventos) {
		this.assinanteEventos = assinanteEventos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endpoint == null) ? 0 : endpoint.hashCode());
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
		DestinatarioTO other = (DestinatarioTO) obj;
		if (endpoint == null) {
			if (other.endpoint != null)
				return false;
		} else if (!endpoint.equals(other.endpoint))
			return false;
		return true;
	}
	
}
