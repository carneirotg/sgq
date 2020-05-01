package net.sgq.incidentes.artefatos.servicos;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import net.sgq.incidentes.artefatos.modelos.Artefato;
import net.sgq.incidentes.artefatos.modelos.ArtefatoRepository;
import net.sgq.incidentes.artefatos.modelos.to.ArtefatoIdTO;
import net.sgq.incidentes.artefatos.modelos.to.ArtefatoTO;
import net.sgq.incidentes.utils.EntityNotFoundException;

@Service
public class ArtefatoServiceImpl implements ArtefatoService {

	@Autowired
	private ArtefatoRepository repository;

	private Logger logger = LoggerFactory.getLogger(ArtefatoServiceImpl.class);

	@Override
	public ArtefatoIdTO buscaArtefatoPor(Long id) {
		Optional<Artefato> oArtefato = this.repository.findById(id);

		if (oArtefato.isEmpty()) {
			return null;
		}

		return oArtefato.get().toTOId();
	}

	@Override
	public List<ArtefatoIdTO> buscaArtefatos(String nome, Integer pagina, Integer registros) {
		PageRequest page = PageRequest.of(pagina - 1, registros);

		if (nome == null || "".equals(nome)) {
			return this.repository.findAll(page).stream().map(Artefato::toTOId).collect(Collectors.toList());
		}

		return this.repository.findByNomeContaining(nome, page).stream().map(Artefato::toTOId)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Long salvaArtefato(ArtefatoTO artefatoTO, Long id) {

		Artefato artefato;
		
		if(id == null || id == 0) {
			artefato = novoArtefato(artefatoTO);			
		} else {
			artefato = atualizarArtefato(artefatoTO, id);
		}
		
		return artefato.getId();

	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void depreciaArtefato(Long id) {
		int count = this.repository.setDepreciado(id);
		
		if(count == 0) {
			logger.warn("Update de depreciacao nao realizado para entidade id {}", id);
		}

	}

	private Artefato atualizarArtefato(ArtefatoTO artefatoTO, Long id) {
		Optional<Artefato> oArtefato = this.repository.findById(id);

		if (oArtefato.isEmpty()) {
			throw new EntityNotFoundException(Artefato.class.getSimpleName(), id);
		}
		
		Artefato artefato = oArtefato.get();
		
		if(artefato.getDepreciado()) {
			throw new IllegalStateException("Artefato depreciado não pode ser atualizado");
		}
		
		return this.repository.save(artefato.fromTO(artefatoTO));
	}

	private Artefato novoArtefato(ArtefatoTO artefatoTO) {
		return this.repository.save(new Artefato().fromTO(artefatoTO));
	}

	@Override
	public Artefato buscaEntidadeArtefatoPor(Long id) {
		Optional<Artefato> oArtefato = this.repository.findById(id);
		
		if (oArtefato.isEmpty()) {
			return null;
		}

		return oArtefato.get();
	}

}
