package com.kasemodel.goldenraspberryaward.application.impl;

import com.kasemodel.goldenraspberryaward.application.*;
import com.kasemodel.goldenraspberryaward.infra.model.InitialDataVO;
import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Award;
import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Movie;
import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Producer;
import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Studio;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class ImporterServiceImpl implements ImporterServie {
	private final MovieService movieService;
	private final ProducerService producerService;
	private final StudioService studioService;
	private final AwardService awardService;

	@Override
	@Transactional
	public void importData(final InitialDataVO dataVO) {
		final var producers = importProducers(dataVO.getProducers());
		final var studios = importStudios(dataVO.getStudios());
		final var movie = importMovie(dataVO.getTitle(), producers, studios);
		final var award = Award.of(dataVO, movie);
		awardService.save(award);
	}

	private Set<Producer> importProducers(final List<String> producers) {
		return producers.stream()
			.map(Producer::of)
			.map(producerService::validateAndSave)
			.collect(Collectors.toSet());
	}

	private Set<Studio> importStudios(final List<String> studios) {
		return studios.stream()
			.map(Studio::of)
			.map(studioService::validateAndSave)
			.collect(Collectors.toSet());
	}

	private Movie importMovie(final String title, final Set<Producer> producers, final Set<Studio> studios) {
		final var optionalMovie = movieService.findByTitle(title);
		if (optionalMovie.isPresent()) {
			return optionalMovie.get();
		}
		final var movie = Movie.of(title, producers, studios);
		return movieService.create(movie);
	}
}
