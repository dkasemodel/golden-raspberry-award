package com.kasemodel.goldenraspberryaward.application.impl;

import com.kasemodel.goldenraspberryaward.application.StudioService;
import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Studio;
import com.kasemodel.goldenraspberryaward.infra.persistence.repository.StudioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
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
	public Set<Studio> validateAndSaveAll(final Set<Studio> studios) {
		return studios.stream()
			.map(this::validateAndSave)
			.collect(Collectors.toSet());
	}
}
