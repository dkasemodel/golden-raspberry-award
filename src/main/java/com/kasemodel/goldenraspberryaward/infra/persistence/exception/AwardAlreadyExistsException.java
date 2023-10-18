package com.kasemodel.goldenraspberryaward.infra.persistence.exception;

public class AwardAlreadyExistsException extends RuntimeException {
	public AwardAlreadyExistsException(final Long id) {
		super(String.format("award id %d already exists", id));
	}
}
