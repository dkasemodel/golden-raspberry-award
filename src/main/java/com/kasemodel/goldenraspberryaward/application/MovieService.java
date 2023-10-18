package com.kasemodel.goldenraspberryaward.application;

import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Movie;

import java.util.Optional;

public interface MovieService {
//	Movie validateAndSave(Movie movie);

	Movie create(Movie movie);

	Optional<Movie> findByTitle(String title);
}
