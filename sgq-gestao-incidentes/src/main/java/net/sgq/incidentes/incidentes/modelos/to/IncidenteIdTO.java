package net.sgq.incidentes.incidentes.modelos.to;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import net.sgq.incidentes.conformidades.modelos.NaoConformidade;
import net.sgq.incidentes.conformidades.modelos.enums.Estado;
import net.sgq.incidentes.conformidades.modelos.enums.Setor;
import net.sgq.incidentes.incidentes.modelos.enums.ClassificacaoIncidente;
import net.sgq.incidentes.incidentes.modelos.enums.TipoIncidente;

public class IncidenteIdTO extends IncidenteTO {

	@NotNull
	@Positive
	private Long id;

	private List<NaoConformidade> ncsEnvolvidas;
	
	private Date criadoEm;
	private Date concluidoEm;
	
	private Estado situacao;

	public IncidenteIdTO(String titulo, String descricao, String conclusao, Setor setor,
			ClassificacaoIncidente classificacao, TipoIncidente tipoIncidente, Long id,
			List<NaoConformidade> naoConformidades, Date criadoEm, Date concluidoEm, Estado situacao) {
		super(titulo, descricao, conclusao, setor, classificacao, tipoIncidente);
		setId(id);
		setNcsEnvolvidas(naoConformidades);
		setCriadoEm(criadoEm);
		setConcluidoEm(concluidoEm);
		setSituacao(situacao);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<NaoConformidade> getNcsEnvolvidas() {
		return ncsEnvolvidas;
	}

	public void setNcsEnvolvidas(List<NaoConformidade> ncsEnvolvidas) {
		this.ncsEnvolvidas = ncsEnvolvidas;
	}

	public Date getCriadoEm() {
		return criadoEm;
	}

	public void setCriadoEm(Date criadoEm) {
		this.criadoEm = criadoEm;
	}

	public Date getConcluidoEm() {
		return concluidoEm;
	}

	public void setConcluidoEm(Date concluidoEm) {
		this.concluidoEm = concluidoEm;
	}

	public Estado getSituacao() {
		return situacao;
	}

	public void setSituacao(Estado situacao) {
		this.situacao = situacao;
	}
	
}
