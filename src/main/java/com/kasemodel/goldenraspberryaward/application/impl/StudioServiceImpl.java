package com.kasemodel.goldenraspberryaward.application.impl;

import com.kasemodel.goldenraspberryaward.application.StudioService;
import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Studio;
import com.kasemodel.goldenraspberryaward.infra.persistence.exception.ProducerAlreadyExistsException;
import com.kasemodel.goldenraspberryaward.infra.persistence.exception.StudioAlreadyExistsException;
import com.kasemodel.goldenraspberryaward.infra.persistence.exception.StudioNotFoundException;
import com.kasemodel.goldenraspberryaward.infra.persistence.repository.StudioRepository;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.StudioResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class StudioServiceImpl implements StudioService {
	private final StudioRepository repo;

	@Override
	public Studio validateAndSave(final Studio studio) {
		if (log.isDebugEnabled())
			log.debug("Studio - Validating and saving : {}", studio.getName());
		return repo.findByName(studio.getName())
			.orElseGet(() -> repo.save(studio));
	}

	@Override
	public Studio validateAndCreate(String name) throws StudioAlreadyExistsException {
		if (repo.existsByName(name))
			throw new ProducerAlreadyExistsException(name);
		return create(name);
	}

	@Override
	public Optional<Studio> findByExternalId(UUID externalId) {
		return repo.findByExternalIdAndDeletedAtIsNull(externalId);
	}

	@Override
	public List<StudioResponse> findAll() {
		final var studios = repo.findAllByDeletedAtIsNull();
		if (CollectionUtils.isEmpty(studios))
			return Collections.EMPTY_LIST;
		return studios.stream()
			.map(producer -> new StudioResponse(producer.getExternalId(), producer.getName()))
			.collect(Collectors.toList());
	}

	@Override
	public void updateName(UUID externalId, String name) {
		final var studio = repo.findByExternalIdAndDeletedAtIsNull(externalId)
			.orElseThrow(() -> new StudioNotFoundException(externalId));
		studio.setName(name);
		repo.save(studio);
	}

	@Override
	public void delete(UUID externalId) {
		repo.deleteByExternalId(externalId);
	}

	@Transactional
	private Studio create(final String name) {
		return repo.save(Studio.of(name));
	}
}
