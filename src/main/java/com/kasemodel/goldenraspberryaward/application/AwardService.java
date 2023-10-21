package com.kasemodel.goldenraspberryaward.application;

import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Award;
import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Movie;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.award.AwardCreateRequest;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.award.UpdateAwardRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;
import java.util.UUID;

public interface AwardService {
	Award save(Award award);

	Award create(AwardCreateRequest awardCreateRequest);

	Optional<Award> findFetchByExternalId(UUID externalId);

	Page<Award> findAll(PageRequest pageable);

	void update(UUID externalId, UpdateAwardRequest updateRequest);

	void delete(UUID externalId);

	Optional<Movie> findMovie(UUID externalId);
}
