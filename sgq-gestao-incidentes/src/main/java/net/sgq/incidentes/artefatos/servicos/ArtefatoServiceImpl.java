package net.sgq.incidentes.artefatos.servicos;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import net.sgq.incidentes.artefatos.modelos.Artefato;
import net.sgq.incidentes.artefatos.modelos.ArtefatoRepository;

@Service
public class ArtefatoServiceImpl implements ArtefatoService {

	@Autowired
	private ArtefatoRepository repository;

	private Logger logger = LoggerFactory.getLogger(ArtefatoServiceImpl.class);

	@Override
	@Cacheable(value = "artefato")
	public Artefato buscaArtefatoPor(Long id) {
		Optional<Artefato> oArtefato = this.repository.findById(id);

		if (oArtefato.isEmpty()) {
			return null;
		}

		return oArtefato.get();
	}

	@Override
	public Page<Artefato> buscaArtefatos(String nome, Integer pagina, Integer registros) {
		PageRequest page = PageRequest.of(pagina - 1, registros);

		if (nome == null || "".equals(nome)) {
			return this.repository.findAll(page);
		}

		return this.repository.findByNomeContainingIgnoreCase(nome, page);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value = "artefatoPorID", key = "#id")
	public Long salvaArtefato(Artefato artefato, Long id) {

		Long artefatoId;

		if (id == null || id == 0) {
			artefatoId = novoArtefato(artefato).getId();
		} else {
			artefatoId = atualizarArtefato(artefato, id).getId();
		}

		return artefatoId;

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value = "artefatoID", key = "#id")
	public void depreciaArtefato(Long id) {
		int count = this.repository.setDepreciado(id);

		if (count == 0) {
			logger.warn("Update de depreciacao nao realizado para entidade id {}", id);
		}

	}

	private Artefato atualizarArtefato(Artefato artefato, Long id) {
		Optional<Artefato> oArtefato = this.repository.findById(id);

		if (oArtefato.isEmpty()) {
			throw new EntityNotFoundException(String.format("Entidade do tipo 'Artefato' não encontrada para Id %d", id));
		}

		Artefato dbArtefato = oArtefato.get();

		if (dbArtefato.getDepreciado()) {
			throw new IllegalStateException("Artefato depreciado não pode ser atualizado");
		}

		return this.repository.save(artefato);
	}

	private Artefato novoArtefato(Artefato artefato) {
		return this.repository.save(artefato);
	}

}
