package net.sgq.transparencia.comunicacao.modelos.to;

import javax.validation.constraints.NotBlank;

import com.sun.istack.NotNull;

import net.sgq.transparencia.comunicacao.modelos.Comunicado;
import net.sgq.transparencia.comunicacao.modelos.Destinatario;
import net.sgq.transparencia.comunicacao.modelos.enums.TipoDestinatario;

public class DestinatarioTO  implements Comunicado {

	private Long id;

	@NotBlank
	private String descricao;
	
	@NotBlank
	private String endpoint;
	
	@NotNull
	private TipoDestinatario tipoDestinatario;
	
	@NotNull
	private Boolean assinanteRecall;
	
	@NotNull
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

	public TipoDestinatario getTipoDestinatario() {
		return tipoDestinatario;
	}

	public void setTipoDestinatario(TipoDestinatario tipoDestinatario) {
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
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
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
		DestinatarioTO other = (DestinatarioTO) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}
	
}
