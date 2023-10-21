package com.kasemodel.goldenraspberryaward.application.impl;

import com.kasemodel.goldenraspberryaward.application.MovieService;
import com.kasemodel.goldenraspberryaward.application.ProducerService;
import com.kasemodel.goldenraspberryaward.application.StudioService;
import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Movie;
import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Producer;
import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Studio;
import com.kasemodel.goldenraspberryaward.infra.persistence.exception.MovieAlreadyExistsException;
import com.kasemodel.goldenraspberryaward.infra.persistence.exception.movie.MovieNotFoundException;
import com.kasemodel.goldenraspberryaward.infra.persistence.exception.movie.MovieWithoutProducersException;
import com.kasemodel.goldenraspberryaward.infra.persistence.exception.movie.MovieWithoutStudiosException;
import com.kasemodel.goldenraspberryaward.infra.persistence.exception.movie.MovieWithoutTitleException;
import com.kasemodel.goldenraspberryaward.infra.persistence.repository.MovieRepository;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.CreateMovieRequest;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.ProducerResponse;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.StudioResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class MovieServiceImpl implements MovieService {
	private final MovieRepository repo;
	private final ProducerService producerService;
	private final StudioService studioService;

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

	@Override
	public Movie validateAndCreate(final CreateMovieRequest movieRequest) {
		if (log.isDebugEnabled()) {
			log.debug("Validate and create new movie");
		}
		validate(movieRequest);
		validateMovieExistenceByTitle(movieRequest.title());
		final var producers = getOrCreateProducers(movieRequest.producers());
		final var studios = getOrCreateStudios(movieRequest.studios());
		if (log.isDebugEnabled()) {
			log.debug("New movie data:");
			log.debug(" -> Title: {}", movieRequest.title());
			log.debug(" -> Producers: {}",
				producers.stream().map(Producer::getName).reduce((producerNames, producerName) -> producerNames + ", " + producerName));
			log.debug(" -> Studios: {}",
				studios.stream().map(Studio::getName).reduce((studioNames, studioName) -> studioNames + ", " + studioName));
		}
		return repo.save(Movie.of(movieRequest.title(), producers, studios));
	}

	@Override
	public Optional<Movie> findByExternalId(final UUID externalId) {
		return repo.findFetchByExternalIdAndDeletedAtIsNull(externalId);
	}

	@Override
	public Page<Movie> findAll(final PageRequest pageable) {
		return repo.findAll(pageable);
	}

	@Override
	public void delete(final UUID externalId) {
		repo.deleteByExternalId(externalId);
	}

	@Override
	public void updateTitle(UUID externalId, String title) {
		final var movie = repo.findByExternalId(externalId)
			.orElseThrow(() -> new MovieNotFoundException(externalId));
		movie.setTitle(title);
		repo.save(movie);
	}

	@Override
	public Optional<List<Producer>> findAllProducers(final UUID externalId) {
		validateMovieExistenceByExternalId(externalId);
		return repo.findAllProducersByExternalId(externalId);
	}

	private void validateMovieExistenceByExternalId(final UUID externalId) {
		if (!existsByExternalId(externalId))
			throw new MovieNotFoundException(externalId);
	}

	@Override
	public Optional<List<Studio>> findAllStudios(final UUID externalId) {
		validateMovieExistenceByExternalId(externalId);
		return repo.findAllStudiosByExternalId(externalId);
	}

	@Override
	public boolean existsByExternalId(final UUID externalId) {
		return repo.existsByExternalId(externalId);
	}

	private Set<Studio> getOrCreateStudios(final Set<StudioResponse> studios) {
		return studios.stream()
			.map(studioService::getOrCreate)
			.collect(Collectors.toSet());
	}

	private Set<Producer> getOrCreateProducers(final Set<ProducerResponse> producers) {
		return producers.stream()
			.map(producerService::getOrCreate)
			.collect(Collectors.toSet());
	}

	private void validate(final CreateMovieRequest movieRequest) {
		validateTitle(movieRequest);
		validateProducers(movieRequest.producers());
		validateStudios(movieRequest.studios());
	}

	private static void validateTitle(final CreateMovieRequest movieRequest) {
		if (StringUtils.isBlank(movieRequest.title()))
			throw new MovieWithoutTitleException();
	}

	private static void validateProducers(final Set<ProducerResponse> producers) {
		if (CollectionUtils.isEmpty(producers))
			throw new MovieWithoutProducersException();
	}

	private static void validateStudios(final Set<StudioResponse> studios) {
		if (CollectionUtils.isEmpty(studios))
			throw new MovieWithoutStudiosException();
	}

	private void validateMovieExistenceByTitle(final String title) {
		if (log.isDebugEnabled())
			log.debug("Validating the existence of the movie '{}'", title);
		if (repo.existsByTitle(title))
			throw new MovieAlreadyExistsException(title);
	}
}
