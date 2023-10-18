package com.kasemodel.goldenraspberryaward.infra.persistence.exception;

public class ProducerAlreadyExistsException extends RuntimeException {
	public ProducerAlreadyExistsException(final String name) {
		super(String.format("user %s already exists", name));
	}
}
