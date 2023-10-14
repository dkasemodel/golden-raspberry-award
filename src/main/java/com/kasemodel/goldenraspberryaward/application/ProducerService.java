package com.kasemodel.goldenraspberryaward.application;

import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Producer;
import jakarta.persistence.Tuple;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProducerService {
//	Optional<Producer> findByExternalId(UUID externalId);
//    Optional<Producer> findByName(String name);
	Producer validateAndSave(Producer producer);

	Set<Producer> validateAndSaveAll(Set<Producer> producers);

	Optional<List<Tuple>> findWinners();
}
