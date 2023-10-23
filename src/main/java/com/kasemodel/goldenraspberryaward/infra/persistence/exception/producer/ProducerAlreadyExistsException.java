package com.kasemodel.goldenraspberryaward.infra.persistence.exception.producer;

import com.kasemodel.goldenraspberryaward.infra.persistence.exception.RecordAlreadyExistsException;

public class ProducerAlreadyExistsException extends RecordAlreadyExistsException {
	private static final String ENTITY = "Producer";

	public ProducerAlreadyExistsException(final String name) {
		super(ENTITY, name);
	}
}
