package com.kasemodel.goldenraspberryaward.application;

import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Studio;
import com.kasemodel.goldenraspberryaward.infra.persistence.exception.studio.StudioAlreadyExistsException;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.StudioResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

public interface StudioService extends PageableService {
	Studio validateAndSave(Studio studio);

	Studio validateAndCreate(String name)
		throws StudioAlreadyExistsException;

	Studio findByExternalId(UUID externalId);

	Page<Studio> findAll(PageRequest pageable);

	void updateName(UUID externalId, String name);

	void delete(UUID externalId);

	Studio getOrCreate(StudioResponse studioResponse);
}
