package com.kasemodel.goldenraspberryaward.infra.persistence.exception;

public class MovieAlreadyExistsException extends RecordAlreadyExistsException {
	private static final String ENTITY = "movie";

	public MovieAlreadyExistsException(final String title) {
		super(ENTITY, title);
	}
}
