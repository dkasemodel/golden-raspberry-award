package com.kasemodel.goldenraspberryaward.application;

import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Studio;

import java.util.Set;

public interface StudioService {
	Studio validateAndSave(Studio studio);

	Set<Studio> validateAndSaveAll(Set<Studio> studios);
}
