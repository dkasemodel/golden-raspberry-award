package com.kasemodel.goldenraspberryaward.infra.persistence.exception;

public class RecordAlreadyExistsException extends RuntimeException {
	public RecordAlreadyExistsException(final String entity, final String data) {
		super(String.format("%s %s already exists", entity, data));
	}
}
