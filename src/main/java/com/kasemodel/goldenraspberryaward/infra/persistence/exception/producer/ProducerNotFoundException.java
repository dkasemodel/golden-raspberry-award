package com.kasemodel.goldenraspberryaward.infra.persistence.exception.producer;

import com.kasemodel.goldenraspberryaward.infra.persistence.exception.RecordNotFoundException;

import java.util.UUID;

public class ProducerNotFoundException extends RecordNotFoundException {
	private static final String ENTITY = "Producer";

	public ProducerNotFoundException(final UUID externalId) {
		super(ENTITY, externalId);
	}
}
