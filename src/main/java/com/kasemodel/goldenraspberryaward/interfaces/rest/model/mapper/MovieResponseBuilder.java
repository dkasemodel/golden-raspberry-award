package com.kasemodel.goldenraspberryaward.interfaces.rest.model.mapper;

import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Movie;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.MovieResponse;

import java.util.stream.Collectors;

public final class MovieResponseBuilder {
	private MovieResponseBuilder() {
		throw new UnsupportedOperationException("Utility class cannot be instantiated");
	}

	public static MovieResponse buildWithhAllData(final Movie movie) {
		return new MovieResponse(
			movie.getExternalId(),
			movie.getTitle(),
			movie.getProducers().stream().map(ProducerResponseBuilder::build).collect(Collectors.toSet()),
			movie.getStudios().stream().map(StudioResponseBuilder::build).collect(Collectors.toSet())
		);
	}

	public static MovieResponse buildWithExternalIdAndTitle(Movie movie) {
		return new MovieResponse(
			movie.getExternalId(),
			movie.getTitle()
		);
	}
}
