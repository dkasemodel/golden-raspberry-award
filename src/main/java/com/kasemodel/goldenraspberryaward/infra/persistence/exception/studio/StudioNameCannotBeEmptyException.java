package com.kasemodel.goldenraspberryaward.infra.persistence.exception.studio;

public class StudioNameCannotBeEmptyException extends RuntimeException {
	private static final String MESSAGE = "Studio name cannot be empty";
	public StudioNameCannotBeEmptyException() {
		super(MESSAGE);
	}
}
