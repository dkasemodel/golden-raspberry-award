package com.kasemodel.goldenraspberryaward.application.impl;

import com.kasemodel.goldenraspberryaward.application.StudioService;
import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Studio;
import com.kasemodel.goldenraspberryaward.infra.persistence.exception.ProducerAlreadyExistsException;
import com.kasemodel.goldenraspberryaward.infra.persistence.exception.studio.StudioAlreadyExistsException;
import com.kasemodel.goldenraspberryaward.infra.persistence.exception.studio.StudioNotFoundException;
import com.kasemodel.goldenraspberryaward.infra.persistence.exception.studio.StudioNameCannotBeEmptyException;
import com.kasemodel.goldenraspberryaward.infra.persistence.repository.StudioRepository;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.StudioResponse;
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
public class StudioServiceImpl extends PageableServiceImpl implements StudioService {
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
		validate(name);
		if (repo.existsByName(name))
			throw new ProducerAlreadyExistsException(name);
		return create(name);
	}

	private void validate(String name) {
		if (StringUtils.isBlank(name))
			throw new StudioNameCannotBeEmptyException();
	}

	@Override
	public Studio findByExternalId(final UUID externalId) {
		return repo.findByExternalIdAndDeletedAtIsNull(externalId)
			.orElseThrow(() -> new StudioNotFoundException(externalId));
	}

	@Override
	public Page<Studio> findAll(final PageRequest pageable) {
		return repo.findAllByDeletedAtIsNull(pageable);
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

	@Override
	public Studio getOrCreate(final StudioResponse studio) {
		return repo.findByExternalIdAndDeletedAtIsNull(studio.externalId())
			.orElseGet(() -> create(studio.name()));
	}

	@Transactional
	private Studio create(final String name) {
		return repo.save(Studio.of(name));
	}
}
