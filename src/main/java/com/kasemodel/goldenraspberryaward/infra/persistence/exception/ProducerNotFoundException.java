package com.kasemodel.goldenraspberryaward.infra.persistence.exception;

import java.util.UUID;

public class ProducerNotFoundException extends Exception {
	public ProducerNotFoundException(final UUID uuid) {
		super("Producer not found with UUID " + uuid);
	}
}
