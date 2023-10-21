package com.kasemodel.goldenraspberryaward.infra.persistence.exception.movie;

import com.kasemodel.goldenraspberryaward.infra.persistence.exception.RecordNotFoundException;

import java.util.UUID;

public class MovieNotFoundException extends RecordNotFoundException {
	private static final String ENTITY = "Movie";

	public MovieNotFoundException(UUID data) {
		super(ENTITY, data);
	}
}
