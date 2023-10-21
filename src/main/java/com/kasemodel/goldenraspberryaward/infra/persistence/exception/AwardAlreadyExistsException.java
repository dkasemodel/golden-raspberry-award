package com.kasemodel.goldenraspberryaward.infra.persistence.exception;

import java.util.UUID;

public class AwardAlreadyExistsException extends RuntimeException {
	public AwardAlreadyExistsException(final Long id) {
		super(String.format("award id %d already exists", id));
	}

	public AwardAlreadyExistsException(final Short year, final UUID movieExternalId) {
		super(String.format("Award already exists with year %d and Movie External ID '%s'", year, movieExternalId));
	}
}
