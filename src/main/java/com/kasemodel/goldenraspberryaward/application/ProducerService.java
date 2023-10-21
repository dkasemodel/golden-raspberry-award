package com.kasemodel.goldenraspberryaward.application;

import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Producer;
import com.kasemodel.goldenraspberryaward.infra.persistence.exception.ProducerAlreadyExistsException;
import com.kasemodel.goldenraspberryaward.infra.persistence.exception.ProducerNotFoundException;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.ProducerResponse;
import jakarta.persistence.Tuple;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProducerService {
	Producer validateAndSave(Producer producer);

	Optional<List<Tuple>> findWinners();

	Optional<Producer> findByExternalId(UUID externalId);

	Producer validateAndCreate(String name)
		throws ProducerAlreadyExistsException;

	List<Producer> findAll();

	void updateName(UUID externalId, String name)
		throws ProducerNotFoundException;

	void delete(UUID externalId);

	Producer getOrCreate(ProducerResponse producerResponse);
}
