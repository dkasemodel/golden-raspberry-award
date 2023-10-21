package com.kasemodel.goldenraspberryaward.infra.persistence.exception;

import java.util.UUID;

public class ProducerNotFoundException extends RecordNotFoundException {
	private static final String ENTITY = "Producer";

	public ProducerNotFoundException(final UUID externalId) {
		super(ENTITY, externalId);
	}
}
