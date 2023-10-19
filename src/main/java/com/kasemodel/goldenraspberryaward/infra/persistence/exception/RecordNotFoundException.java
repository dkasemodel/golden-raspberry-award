package com.kasemodel.goldenraspberryaward.infra.persistence.exception;

import java.util.UUID;

public class RecordNotFoundException extends RuntimeException {
	public RecordNotFoundException(final String entity, final UUID data) {
		super(String.format("%s not found with ExternalId %s", entity, data));
	}
}
