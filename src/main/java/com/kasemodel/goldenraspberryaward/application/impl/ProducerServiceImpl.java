package com.kasemodel.goldenraspberryaward.application.impl;

import com.kasemodel.goldenraspberryaward.application.ProducerService;
import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Producer;
import com.kasemodel.goldenraspberryaward.infra.persistence.repository.ProducerRepository;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProducerServiceImpl implements ProducerService {
	private final ProducerRepository repo;

	@Override
	public Producer validateAndSave(final Producer producer) {
		log.info("Producer: '{}'", producer.getName());
//		return repo.findByName(producer.getName())
//			.orElse(repo.save(producer));
		final var optionalProducer = repo.findByName(producer.getName());
		if (optionalProducer.isPresent())
			return optionalProducer.get();
		return repo.save(producer);
	}

	@Override
	public Set<Producer> validateAndSaveAll(final Set<Producer> producers) {
		return producers.stream()
			.map(this::validateAndSave)
			.collect(Collectors.toSet());
	}

	@Override
	public Optional<List<Tuple>> findWinners() {
		return repo.findWinners();
	}

//	@Override
//	public Optional<Producer> findByExternalId(UUID externalId) {
////		return repo.findByExternalId(externalId);
//		return null;
//	}
//
//	@Override
//	public Optional<Producer> findByName(String name) {
//		return Optional.empty();
//	}
}
