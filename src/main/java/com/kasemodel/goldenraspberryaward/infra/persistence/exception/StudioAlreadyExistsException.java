package com.kasemodel.goldenraspberryaward.infra.persistence.exception;

public class StudioAlreadyExistsException extends RecordAlreadyExistsException {
	private static final String ENTITY = "studio";

	public StudioAlreadyExistsException(final String name) {
		super(ENTITY, name);
	}
}
