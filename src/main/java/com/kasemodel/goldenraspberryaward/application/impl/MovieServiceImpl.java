package com.kasemodel.goldenraspberryaward.application.impl;

import com.kasemodel.goldenraspberryaward.application.MovieService;
import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Movie;
import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Producer;
import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Studio;
import com.kasemodel.goldenraspberryaward.infra.persistence.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class MovieServiceImpl implements MovieService {
	private final MovieRepository repo;

//	@Override
//	public Movie validateAndSave(final Movie movie) {
//		if (log.isDebugEnabled())
//			log.debug("Movie - Validating and saving : {}", movie.toString());
//		return repo.findByTitle(movie.getTitle())
//			.orElseGet(() -> repo.save(movie));
//	}

	@Override
	public Movie create(final Movie movie) {
		if (log.isDebugEnabled()) {
			log.debug("Movie - Creating new : {}", movie.getTitle());
			log.debug(" - Studios   : {}", movie.getStudios().stream().map(Studio::getName).reduce((studios, studio) -> studios + "," + studio).get());
			log.debug(" - Producers : {}", movie.getProducers().stream().map(Producer::getName).reduce((producers, producer) -> producers + "," + producer).get());
		}
		return repo.save(movie);
	}

	@Override
	public Optional<Movie> findByTitle(final String title) {
		return repo.findByTitle(title);
	}
}
