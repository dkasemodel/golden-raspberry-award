package com.kasemodel.goldenraspberryaward.infra.persistence.exception.producer;

public class ProducerNameCannotBeEmptyException extends RuntimeException {
	private static final String MESSAGE = "Producer name cannot be empty";

	public ProducerNameCannotBeEmptyException() {
		super(MESSAGE);
	}
}
