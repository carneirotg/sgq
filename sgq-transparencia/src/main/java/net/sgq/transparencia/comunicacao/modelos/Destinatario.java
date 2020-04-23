package net.sgq.transparencia.comunicacao.modelos;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import net.sgq.transparencia.comunicacao.modelos.enums.TipoDestinatatio;
import net.sgq.transparencia.comunicacao.modelos.to.DestinatarioTO;

@Entity
@Table(name = "destinatarios")
public class Destinatario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String descricao;
	
	@Column
	private String endpoint;
	
	@Column
	@Enumerated(EnumType.STRING)
	private TipoDestinatatio tipoDestinatario;
	
	@Column(nullable = false)
	private Boolean assinanteRecall;
	
	@Column(nullable = false)
	private Boolean assinanteEventos;

	public Destinatario() {
		super();
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
	
	public DestinatarioTO toTO() {
		return new DestinatarioTO(this);
	}
	
	public Destinatario fromTO(DestinatarioTO to) {
		setAssinanteEventos(to.getAssinanteEventos());
		setAssinanteRecall(to.getAssinanteEventos());
		setDescricao(to.getDescricao());
		setEndpoint(to.getEndpoint());
		setTipoDestinatario(to.getTipoDestinatario());
		
		return this;
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
		Destinatario other = (Destinatario) obj;
		if (endpoint == null) {
			if (other.endpoint != null)
				return false;
		} else if (!endpoint.equals(other.endpoint))
			return false;
		return true;
	}
	
}
