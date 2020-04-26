package net.sgq.incidentes.conformidades.modelos;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.sun.istack.NotNull;

import net.sgq.incidentes.artefatos.modelos.Artefato;
import net.sgq.incidentes.conformidades.modelos.enums.Estado;
import net.sgq.incidentes.conformidades.modelos.enums.Setor;
import net.sgq.incidentes.conformidades.modelos.enums.TipoNaoConformidade;
import net.sgq.incidentes.conformidades.modelos.to.NaoConformidadeIdTO;
import net.sgq.incidentes.conformidades.modelos.to.NaoConformidadeTO;
import net.sgq.incidentes.utils.TOAble;

@Entity
@Table(name = "nao_conformidades")
public class NaoConformidade implements TOAble<NaoConformidade, NaoConformidadeTO, NaoConformidadeIdTO> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Column(length = 100, nullable = false)
	private String titulo;

	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(nullable = false)
	private TipoNaoConformidade tipoNaoConformidade;

	@NotBlank
	@Column(length = 300, nullable = false)
	private String resumo;

	@NotBlank
	@Lob
	@Column(nullable = false)
	private String detalhamentoNaoConformidade;

	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(nullable = false)
	private Setor setor;

	@ManyToOne
	private Artefato artefato;

	@NotBlank
	@Column(length = 200, nullable = false)
	private String detalhamentoArtefato;

	@Column(nullable = false)
	private Boolean prejuizoApurado = Boolean.FALSE;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Estado estado = Estado.ABERTA;

	@Embedded
	private Norma normaNaoConformidade;

	public NaoConformidade() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public TipoNaoConformidade getTipoNaoConformidade() {
		return tipoNaoConformidade;
	}

	public void setTipoNaoConformidade(TipoNaoConformidade tipoNaoConformidade) {
		this.tipoNaoConformidade = tipoNaoConformidade;
	}

	public String getResumo() {
		return resumo;
	}

	public void setResumo(String resumo) {
		this.resumo = resumo;
	}

	public String getDetalhamentoNaoConformidade() {
		return detalhamentoNaoConformidade;
	}

	public void setDetalhamentoNaoConformidade(String detalhamentoNaoConformidade) {
		this.detalhamentoNaoConformidade = detalhamentoNaoConformidade;
	}

	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	public Artefato getArtefato() {
		return artefato;
	}

	public void setArtefato(Artefato artefato) {
		this.artefato = artefato;
	}

	public String getDetalhamentoArtefato() {
		return detalhamentoArtefato;
	}

	public void setDetalhamentoArtefato(String detalhamentoArtefato) {
		this.detalhamentoArtefato = detalhamentoArtefato;
	}

	public Boolean getPrejuizoApurado() {
		return prejuizoApurado;
	}

	public void setPrejuizoApurado(Boolean prejuizoApurado) {
		this.prejuizoApurado = prejuizoApurado;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Norma getNormaNaoConformidade() {
		return normaNaoConformidade;
	}

	public void setNormaNaoConformidade(Norma normaNaoConformidade) {
		this.normaNaoConformidade = normaNaoConformidade;
	}

	@Override
	public NaoConformidadeTO toTO() {
		return new NaoConformidadeTO(getTitulo(), getTipoNaoConformidade(), getResumo(),
				getDetalhamentoNaoConformidade(), getSetor(), getArtefato().getId(), getDetalhamentoArtefato(),
				getPrejuizoApurado());
	}

	@Override
	public NaoConformidadeIdTO toTOId() {
		return new NaoConformidadeIdTO(getTitulo(), getTipoNaoConformidade(), getResumo(),
				getDetalhamentoNaoConformidade(), getSetor(), getArtefato().getId(), getDetalhamentoArtefato(),
				getPrejuizoApurado(), getId(), getEstado(), getNormaNaoConformidade());
	}

	@Override
	public NaoConformidade fromTO(NaoConformidadeTO to) {
		setDetalhamentoArtefato(to.getDetalhamentoArtefato());
		setDetalhamentoNaoConformidade(to.getDetalhamentoNaoConformidade());
		setPrejuizoApurado(to.getPrejuizoApurado());
		setResumo(to.getResumo());
		setSetor(to.getSetor());
		setTipoNaoConformidade(to.getTipoNaoConformidade());
		setTitulo(to.getTitulo());
		
		return this;
	}

	@Override
	public NaoConformidade fromTOID(NaoConformidadeIdTO toid) {
		fromTO(toid);
		setId(toid.getId());
		
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((artefato == null) ? 0 : artefato.hashCode());
		result = prime * result + ((detalhamentoArtefato == null) ? 0 : detalhamentoArtefato.hashCode());
		result = prime * result + ((detalhamentoNaoConformidade == null) ? 0 : detalhamentoNaoConformidade.hashCode());
		result = prime * result + ((estado == null) ? 0 : estado.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((normaNaoConformidade == null) ? 0 : normaNaoConformidade.hashCode());
		result = prime * result + ((prejuizoApurado == null) ? 0 : prejuizoApurado.hashCode());
		result = prime * result + ((resumo == null) ? 0 : resumo.hashCode());
		result = prime * result + ((setor == null) ? 0 : setor.hashCode());
		result = prime * result + ((tipoNaoConformidade == null) ? 0 : tipoNaoConformidade.hashCode());
		result = prime * result + ((titulo == null) ? 0 : titulo.hashCode());
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
		NaoConformidade other = (NaoConformidade) obj;
		if (artefato == null) {
			if (other.artefato != null) {
				return false;
			}
		} else if (!artefato.equals(other.artefato)) {
			return false;
		}
		if (detalhamentoArtefato == null) {
			if (other.detalhamentoArtefato != null) {
				return false;
			}
		} else if (!detalhamentoArtefato.equals(other.detalhamentoArtefato)) {
			return false;
		}
		if (detalhamentoNaoConformidade == null) {
			if (other.detalhamentoNaoConformidade != null) {
				return false;
			}
		} else if (!detalhamentoNaoConformidade.equals(other.detalhamentoNaoConformidade)) {
			return false;
		}
		if (estado != other.estado) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (normaNaoConformidade == null) {
			if (other.normaNaoConformidade != null) {
				return false;
			}
		} else if (!normaNaoConformidade.equals(other.normaNaoConformidade)) {
			return false;
		}
		if (prejuizoApurado == null) {
			if (other.prejuizoApurado != null) {
				return false;
			}
		} else if (!prejuizoApurado.equals(other.prejuizoApurado)) {
			return false;
		}
		if (resumo == null) {
			if (other.resumo != null) {
				return false;
			}
		} else if (!resumo.equals(other.resumo)) {
			return false;
		}
		if (setor != other.setor) {
			return false;
		}
		if (tipoNaoConformidade != other.tipoNaoConformidade) {
			return false;
		}
		if (titulo == null) {
			if (other.titulo != null) {
				return false;
			}
		} else if (!titulo.equals(other.titulo)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "NaoConformidade [id=" + id + ", titulo=" + titulo + ", tipoNaoConformidade=" + tipoNaoConformidade
				+ ", estado=" + estado + "]";
	}

}
