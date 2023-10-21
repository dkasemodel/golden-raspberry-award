package com.kasemodel.goldenraspberryaward.infra.persistence.exception.award;

public class AwardUpdateException extends RuntimeException {
	public AwardUpdateException(final String message) {
		super(message);
	}
}
