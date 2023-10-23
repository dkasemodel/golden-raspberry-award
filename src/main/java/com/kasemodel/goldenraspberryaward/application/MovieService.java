package com.kasemodel.goldenraspberryaward.application;

import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Movie;
import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Producer;
import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Studio;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.CreateMovieRequest;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.UpdateNameRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MovieService extends PageableService {
	Movie create(Movie movie);

	Optional<Movie> findByTitle(String title);

	Movie validateAndCreate(CreateMovieRequest movie);

	Movie findByExternalId(UUID externalId);

	Page<Movie> findAll(PageRequest pageable);

	void delete(UUID externalId);

	void updateTitle(UUID externalId, UpdateNameRequest updateRequest);

	Optional<List<Producer>> findAllProducers(UUID externalId);

	Optional<List<Studio>> findAllStudios(UUID externalId);

	boolean existsByExternalId(UUID uuid);
}
