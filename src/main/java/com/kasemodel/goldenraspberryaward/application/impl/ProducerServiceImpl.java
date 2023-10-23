package com.kasemodel.goldenraspberryaward.application.impl;

import com.kasemodel.goldenraspberryaward.application.ProducerService;
import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Producer;
import com.kasemodel.goldenraspberryaward.infra.persistence.exception.producer.ProducerAlreadyExistsException;
import com.kasemodel.goldenraspberryaward.infra.persistence.exception.producer.ProducerNameCannotBeEmptyException;
import com.kasemodel.goldenraspberryaward.infra.persistence.exception.producer.ProducerNotFoundException;
import com.kasemodel.goldenraspberryaward.infra.persistence.repository.ProducerRepository;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.CreateByNameRequest;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.ProducerResponse;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProducerServiceImpl extends PageableServiceImpl implements ProducerService {
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
	public Producer findByExternalId(final UUID externalId) {
		return repo.findByExternalIdAndDeletedAtIsNull(externalId)
			.orElseThrow(() -> new ProducerNotFoundException(externalId));
	}

	@Override
	public Producer validateAndCreate(final CreateByNameRequest producer) throws ProducerAlreadyExistsException {
		validate(producer);
		return create(producer.name());
	}

	@Override
	public Page<Producer> findAll(final PageRequest pageable) {
		return repo.findAllByDeletedAtIsNull(pageable);
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
		if (!repo.existsByExternalId(externalId))
			throw new ProducerNotFoundException(externalId);
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

	private void validate(final CreateByNameRequest producer) {
		if (producer == null || StringUtils.isBlank(producer.name()))
			throw new ProducerNameCannotBeEmptyException();
		if (repo.existsByName(producer.name()))
			throw new ProducerAlreadyExistsException(producer.name());
	}
}
