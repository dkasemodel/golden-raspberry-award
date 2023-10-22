package com.kasemodel.goldenraspberryaward.application.impl;

import com.kasemodel.goldenraspberryaward.application.ProducerService;
import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Producer;
import com.kasemodel.goldenraspberryaward.infra.persistence.exception.ProducerAlreadyExistsException;
import com.kasemodel.goldenraspberryaward.infra.persistence.exception.ProducerNotFoundException;
import com.kasemodel.goldenraspberryaward.infra.persistence.repository.ProducerRepository;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.ProducerResponse;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProducerServiceImpl implements ProducerService {
	private final ProducerRepository repo;

	@Override
	public Producer validateAndSave(final Producer producer) {
		if (log.isDebugEnabled())
			log.debug("Producer - Validating and saving : {}", producer.getName());
		return repo.findByName(producer.getName())
			.orElseGet(() -> repo.save(producer));
	}

	@Override
	public List<Tuple> findWinners() {
		return repo.findWinners();
	}

	@Override
	public Optional<Producer> findByExternalId(final UUID externalId) {
		return repo.findByExternalIdAndDeletedAtIsNull(externalId);
	}

	@Override
	public Producer validateAndCreate(final String name) throws ProducerAlreadyExistsException {
		if (repo.existsByName(name))
			throw new ProducerAlreadyExistsException(name);
		return create(name);
	}

	@Override
	public List<Producer> findAll() {
		return repo.findAllByDeletedAtIsNull()
			.orElseGet(Collections::emptyList);
	}

	@Override
	@Transactional
	public void updateName(final UUID externalId, final String name) throws ProducerNotFoundException {
		final var producer = repo.findByExternalIdAndDeletedAtIsNull(externalId)
			.orElseThrow(() -> new ProducerNotFoundException(externalId));
		producer.setName(name);
		repo.save(producer);
	}

	@Override
	@Transactional
	public void delete(UUID externalId) {
		repo.deleteByExternalId(externalId);
	}

	@Override
	public Producer getOrCreate(ProducerResponse producer) {
		return repo.findByExternalIdAndDeletedAtIsNull(producer.externalId())
			.orElseGet(() -> create(producer.name()));
	}

	@Transactional
	private Producer create(final String name) {
		return repo.save(Producer.of(name));
	}
}
