package com.kasemodel.goldenraspberryaward.application.impl;

import com.kasemodel.goldenraspberryaward.application.AwardService;
import com.kasemodel.goldenraspberryaward.application.MovieService;
import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Award;
import com.kasemodel.goldenraspberryaward.infra.persistence.repository.AwardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AwardServiceImpl implements AwardService {
	private final AwardRepository repo;
	private final MovieService movieService;

	@Override
	public Award create(final Award award) {
		if (award.getId() != null)
			throw new IllegalArgumentException("Award already exists");
		movieService.create(award.getMovie());
		return repo.save(award);
	}

	@Override
	public Award save(final Award award) {
		return repo.save(award);
	}
}
