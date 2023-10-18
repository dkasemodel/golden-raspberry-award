package com.kasemodel.goldenraspberryaward.application.impl;

import com.kasemodel.goldenraspberryaward.application.AwardService;
import com.kasemodel.goldenraspberryaward.application.MovieService;
import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Award;
import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Producer;
import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Studio;
import com.kasemodel.goldenraspberryaward.infra.persistence.exception.AwardAlreadyExistsException;
import com.kasemodel.goldenraspberryaward.infra.persistence.repository.AwardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class AwardServiceImpl implements AwardService {
	private final AwardRepository repo;
	private final MovieService movieService;

//	@Override
//	public Award create(final Award award) throws AwardAlreadyExistsException {
//		validateAndLogDebugData(award);
//		if (award.getId() != null)
//			throw new AwardAlreadyExistsException(award.getId());
//		movieService.create(award.getMovie());
//		return repo.save(award);
//	}

	@Override
	public Award save(final Award award) {
		validateAndLogDebugData(award);
		return repo.save(award);
	}

	private void validateAndLogDebugData(Award award) {
		if (log.isDebugEnabled()) {
			log.debug("Award - Creating new one");
			log.debug(" - Year      : {}", award.getYear());
			log.debug(" - Movie     : {}", award.getMovie().getTitle());
			log.debug(" - Studios   : {}", award.getMovie().getStudios().stream().map(Studio::getName).reduce((studios, studio) -> studios + "," + studio).get());
			log.debug(" - Producers : {}", award.getMovie().getProducers().stream().map(Producer::getName).reduce((producers, producer) -> producers + "," + producer).get());
			log.debug(" - Winner    : {}", award.getWinner());
		}
	}
}
