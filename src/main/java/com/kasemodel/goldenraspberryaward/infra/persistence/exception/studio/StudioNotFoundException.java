package com.kasemodel.goldenraspberryaward.infra.persistence.exception.studio;

import com.kasemodel.goldenraspberryaward.infra.persistence.exception.RecordNotFoundException;

import java.util.UUID;

public class StudioNotFoundException extends RecordNotFoundException {
	private static final String ENTITY = "Studio";

	public StudioNotFoundException(UUID externalId) {
		super(ENTITY, externalId);
	}
}
