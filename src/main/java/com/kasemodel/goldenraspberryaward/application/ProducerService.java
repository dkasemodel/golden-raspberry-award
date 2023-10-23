package com.kasemodel.goldenraspberryaward.application;

import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Producer;
import com.kasemodel.goldenraspberryaward.infra.persistence.exception.producer.ProducerAlreadyExistsException;
import com.kasemodel.goldenraspberryaward.infra.persistence.exception.producer.ProducerNotFoundException;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.CreateByNameRequest;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.ProducerResponse;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.UUID;

public interface ProducerService extends PageableService {
	Producer validateAndSave(Producer producer);

	List<Tuple> findWinners();

	Producer findByExternalId(UUID externalId);

	Producer validateAndCreate(CreateByNameRequest producer)
		throws ProducerAlreadyExistsException;

	Page<Producer> findAll(PageRequest pageable);

	void updateName(UUID externalId, String name)
		throws ProducerNotFoundException;

	void delete(UUID externalId);

	Producer getOrCreate(ProducerResponse producerResponse);
}
