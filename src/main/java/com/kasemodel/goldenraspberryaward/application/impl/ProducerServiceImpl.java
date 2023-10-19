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
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
	public Set<Producer> validateAndSaveAll(final Set<Producer> producers) {
		return producers.stream()
			.map(this::validateAndSave)
			.collect(Collectors.toSet());
	}

	@Override
	public Optional<List<Tuple>> findWinners() {
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
	public List<ProducerResponse> findAll() {
		final var producers = repo.findAllByDeletedAtIsNull();
		if (CollectionUtils.isEmpty(producers))
			return Collections.EMPTY_LIST;
		return producers.stream()
			.map(producer -> new ProducerResponse(producer.getExternalId(), producer.getName()))
			.collect(Collectors.toList());
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

	@Transactional
	private Producer create(final String name) {
		return repo.save(Producer.of(name));
	}
}
