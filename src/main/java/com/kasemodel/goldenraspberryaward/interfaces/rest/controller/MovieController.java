package com.kasemodel.goldenraspberryaward.interfaces.rest.controller;

import com.kasemodel.goldenraspberryaward.application.MovieService;
import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Movie;
import com.kasemodel.goldenraspberryaward.interfaces.rest.exception.MaxPageSizeException;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.*;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.mapper.MovieResponseBuilder;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.mapper.ProducerResponseBuilder;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.mapper.StudioResponseBuilder;
import com.kasemodel.goldenraspberryaward.interfaces.rest.util.PageableValuesEnum;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/v1/movies")
@RequiredArgsConstructor
@Slf4j
public class MovieController {
	private final MovieService movieService;

	@PostMapping
	@ApiResponses({
		@ApiResponse(responseCode = "201", content = {@Content(schema = @Schema())}),
		@ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())}),
		@ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
	public ResponseEntity create(@RequestBody final CreateMovieRequest movie) {
		final Movie created = movieService.validateAndCreate(movie);
		return ResponseEntity.created(URI.create(String.format("/v1/movies/%s", created.getExternalId()))).build();
	}

	@GetMapping("/{externalId}")
	public ResponseEntity<MovieResponse> findByExternalId(@PathVariable final UUID externalId) {
		final Optional<Movie> optMovie = movieService.findByExternalId(externalId);
		if (optMovie.isPresent()) {
			final var movie = MovieResponseBuilder.buildWithhAllData(optMovie.get());
			return ResponseEntity.ok(movie);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@GetMapping
	public ResponseEntity findAll(
		@RequestParam(value = "page", required = false, defaultValue = "1") final int page,
		@RequestParam(value = "size", required = false, defaultValue = "10") final int size) {
		if (size > PageableValuesEnum.MAX_PAGE_SIZE.getValue())
			throw new MaxPageSizeException(size);
		final var movie = movieService.findAll(PageRequest.of((page - 1), size, Sort.Direction.ASC, "title"))
			.map(MovieResponseBuilder::buildWithhAllData);
		return ResponseEntity.ok(new PageResponse(movie.getContent(), page, size, movie.getTotalPages(), movie.getTotalElements()));
	}

	@PutMapping("/{externalId}")
	public ResponseEntity update(@PathVariable final UUID externalId, @RequestBody final UpdateNameRequest updateRequest) {
		movieService.updateTitle(externalId, updateRequest.name());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@DeleteMapping("/{externalId}")
	public ResponseEntity delete(@PathVariable final UUID externalId) {
		try {
			movieService.delete(externalId);
		} catch (final Exception e) {
			log.error("Error when deleting producer", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@GetMapping("/{externalId}/producers")
	public ResponseEntity<List<ProducerResponse>> findAllProducers(@PathVariable final UUID externalId) {
		final var movieProducers = movieService.findAllProducers(externalId);
		if (movieProducers.isPresent())
			return ResponseEntity.ok(ProducerResponseBuilder.build(movieProducers.get()));
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@GetMapping("/{externalId}/studios")
	public ResponseEntity<List<StudioResponse>> findAllStudios(@PathVariable final UUID externalId) {
		final var movieStudios = movieService.findAllStudios(externalId);
		if (movieStudios.isPresent())
			return ResponseEntity.ok(StudioResponseBuilder.build(movieStudios.get()));
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
