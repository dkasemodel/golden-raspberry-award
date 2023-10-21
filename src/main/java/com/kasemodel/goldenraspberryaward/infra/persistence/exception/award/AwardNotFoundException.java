package com.kasemodel.goldenraspberryaward.infra.persistence.exception.award;

import com.kasemodel.goldenraspberryaward.infra.persistence.exception.RecordNotFoundException;

import java.util.UUID;

public class AwardNotFoundException extends RecordNotFoundException {
	private static final String ENTITY = "Award";

	public AwardNotFoundException(final UUID data) {
		super(ENTITY, data);
	}
}
