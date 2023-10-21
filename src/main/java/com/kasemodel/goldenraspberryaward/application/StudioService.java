package com.kasemodel.goldenraspberryaward.application;

import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Studio;
import com.kasemodel.goldenraspberryaward.infra.persistence.exception.StudioAlreadyExistsException;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.StudioResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StudioService {
	Studio validateAndSave(Studio studio);

//	Set<Studio> validateAndSaveAll(Set<Studio> studios);

	Studio validateAndCreate(String name)
		throws StudioAlreadyExistsException;

	Optional<Studio> findByExternalId(UUID externalId);

	List<Studio> findAll();

	void updateName(UUID externalId, String name);

	void delete(UUID externalId);

	Studio getOrCreate(StudioResponse studioResponse);
}
