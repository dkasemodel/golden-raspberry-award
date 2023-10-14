package com.kasemodel.goldenraspberryaward.application.impl;

import com.kasemodel.goldenraspberryaward.application.MovieService;
import com.kasemodel.goldenraspberryaward.application.ProducerService;
import com.kasemodel.goldenraspberryaward.application.StudioService;
import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Movie;
import com.kasemodel.goldenraspberryaward.infra.persistence.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MovieServiceImpl implements MovieService {
	private final MovieRepository repo;
	private final ProducerService producerService;
	private final StudioService studioService;

	@Override
	public Movie validateAndSave(final Movie movie) {
		return repo.findByTitle(movie.getTitle())
			.orElse(repo.save(movie));
	}

	@Override
	public Movie create(final Movie movie) {
//		createAndSetProducers(movie);
//		createAndSetStudios(movie);
//		final var saved = validateAndSave(movie);
//		saved.setProducers(movie.getProducers());
//		saved.setStudios(movie.getStudios());
//		return saved;
		return repo.save(movie);
	}

	@Override
	public Optional<Movie> findByTitle(final String title) {
		return repo.findByTitle(title);
	}

	private void createAndSetProducers(final Movie movie) {
		final var producers = producerService.validateAndSaveAll(movie.getProducers());
		movie.setProducers(producers);
//		producerService.validateAndSaveAll(movie.getProducers());
	}

	private void createAndSetStudios(final Movie movie) {
		final var studios = studioService.validateAndSaveAll(movie.getStudios());
		movie.setStudios(studios);
//		studioService.validateAndSaveAll(movie.getStudios());
	}
}
