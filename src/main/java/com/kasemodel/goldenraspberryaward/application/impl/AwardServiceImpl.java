package com.kasemodel.goldenraspberryaward.application.impl;

import com.kasemodel.goldenraspberryaward.application.AwardService;
import com.kasemodel.goldenraspberryaward.application.MovieService;
import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Award;
import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Movie;
import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Producer;
import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Studio;
import com.kasemodel.goldenraspberryaward.infra.persistence.exception.AwardAlreadyExistsException;
import com.kasemodel.goldenraspberryaward.infra.persistence.exception.award.AwardNotFoundException;
import com.kasemodel.goldenraspberryaward.infra.persistence.exception.award.AwardUpdateException;
import com.kasemodel.goldenraspberryaward.infra.persistence.exception.award.AwardYearCannotBeEmptyException;
import com.kasemodel.goldenraspberryaward.infra.persistence.exception.movie.MovieNotFoundException;
import com.kasemodel.goldenraspberryaward.infra.persistence.repository.AwardRepository;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.award.AwardCreateRequest;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.award.UpdateAwardRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
@Slf4j
public class AwardServiceImpl implements AwardService {
	private final AwardRepository repo;
	private final MovieService movieService;

	@Override
	public Award save(final Award award) {
		validateAndLogDebugData(award);
		return repo.save(award);
	}

	@Override
	public Award create(final AwardCreateRequest awardCreateRequest) {
		validate(awardCreateRequest);
		final var movie = movieService.findByExternalId(awardCreateRequest.movie().externalId());
		return repo.save(Award.of(awardCreateRequest.year(), awardCreateRequest.winner(), movie));
	}

	@Override
	public Optional<Award> findFetchByExternalId(UUID externalId) {
		return repo.findFetchByExternalId(externalId);
	}

	@Override
	public Page<Award> findAll(PageRequest pageable) {
		return repo.findAll(pageable);
	}

	@Override
	public void update(UUID externalId, UpdateAwardRequest updateRequest) {
		validate(updateRequest);
		final var award = repo.findByExternalId(externalId)
			.orElseThrow(() -> new AwardNotFoundException(externalId));
		final var movie = movieService.findByExternalId(updateRequest.movie().externalId());
		award.setYear(updateRequest.year());
		award.setWinner(Boolean.TRUE.equals(updateRequest.winner()));
		award.setMovie(movie);
		repo.save(award);
	}

	@Override
	public void delete(UUID externalId) {
		if (!repo.existsByExternalId(externalId))
			throw new AwardNotFoundException(externalId);
		repo.deleteByExternalId(externalId);
	}

	@Override
	public Optional<Movie> findMovie(final UUID externalId) {
		return repo.findMovieByExternalId(externalId);
	}

	private void validate(UpdateAwardRequest updateRequest) {
		if (updateRequest.year() == null)
			throw new AwardUpdateException("Year cannot be null");
		if (!Pattern.compile("\\d{4}").matcher(String.valueOf(updateRequest.year())).matches())
			throw new AwardUpdateException("Year must contains 4 digits");
		if (awardExists(updateRequest.year(), updateRequest.movie().externalId()))
			throw new AwardAlreadyExistsException(updateRequest.year(), updateRequest.movie().externalId());
	}

	private void validate(final AwardCreateRequest awardCreateRequest) {
		if (awardCreateRequest.year() == null)
			throw new AwardYearCannotBeEmptyException();
		if (!movieService.existsByExternalId(awardCreateRequest.movie().externalId()))
			throw new MovieNotFoundException(awardCreateRequest.movie().externalId());
		if (awardExists(awardCreateRequest.year(), awardCreateRequest.movie().externalId()))
			throw new AwardAlreadyExistsException(awardCreateRequest.year(), awardCreateRequest.movie().externalId());
	}

	private boolean awardExists(final Short year, final UUID movieExternalId) {
		return repo.existsByYearAndMovieExternalId(year, movieExternalId);
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
