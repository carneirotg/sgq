package net.sgq.transparencia.comunicacao.modelos;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.sgq.transparencia.comunicacao.modelos.to.DestinatarioTO;

@Entity
@Table(name = "envios", indexes = { @Index(columnList = "comunicadoId,comunicadoTipo,destinatarioId") })
public class Envio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long comunicadoId;

	@Column(length = 20, nullable = false)
	private String comunicadoTipo;
	
	@Column(length = 200, nullable = false)
	private String comunicadoTitulo;

	@Column(nullable = false)
	private Long destinatarioId;
	
	private String destinatario;
	
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataEnvio;

	public Envio() {
		super();
	}
	
	public Envio(Comunicado comunicado, DestinatarioTO destinatario) {
		this.comunicadoId = comunicado.getId();
		this.comunicadoTipo = comunicado.getType();
		this.comunicadoTitulo = comunicado.getTitulo();
		this.destinatarioId = destinatario.getId();
		this.destinatario = destinatario.getDescricao();
	}

	@PrePersist
	public void prePersist() {
		this.dataEnvio = new Date();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getComunicadoId() {
		return comunicadoId;
	}

	public void setComunicadoId(Long comunicadoId) {
		this.comunicadoId = comunicadoId;
	}

	public String getComunicadoTipo() {
		return comunicadoTipo;
	}

	public void setComunicadoTipo(String comunicadoTipo) {
		this.comunicadoTipo = comunicadoTipo;
	}

	public String getComunicadoTitulo() {
		return comunicadoTitulo;
	}

	public void setComunicadoTitulo(String comunicadoTitulo) {
		this.comunicadoTitulo = comunicadoTitulo;
	}

	public Long getDestinatarioId() {
		return destinatarioId;
	}

	public void setDestinatarioId(Long destinatarioId) {
		this.destinatarioId = destinatarioId;
	}

	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	public Date getDataEnvio() {
		return dataEnvio;
	}

	public void setDataEnvio(Date dataEnvio) {
		this.dataEnvio = dataEnvio;
	}

	@Override
	public String toString() {
		return "Envio [id=" + id + ", comunicadoId=" + comunicadoId + ", comunicadoTipo=" + comunicadoTipo
				+ ", destinatarioId=" + destinatarioId + ", dataEnvio=" + dataEnvio + "]";
	}

}
