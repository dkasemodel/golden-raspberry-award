package com.kasemodel.goldenraspberryaward.interfaces.rest.controller;

import com.kasemodel.goldenraspberryaward.application.MovieService;
import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Movie;
import com.kasemodel.goldenraspberryaward.interfaces.rest.exception.MaxPageSizeException;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.*;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.mapper.MovieResponseBuilder;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.mapper.ProducerResponseBuilder;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.mapper.StudioResponseBuilder;
import com.kasemodel.goldenraspberryaward.interfaces.rest.util.PageableValuesEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/v1/movies")
@RequiredArgsConstructor
@Tag(name = "Movies", description = "Movies Controller")
@Slf4j
public class MovieController {
	private final MovieService movieService;

	@PostMapping
	@Operation(
		summary = "Create new Movie",
		description = "Create new Movie with the data sent by body. The method will return a status 201 (created) if everything is OK")
	@ApiResponses({
		@ApiResponse(responseCode = "201", content = {@Content(schema = @Schema())}),
		@ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())}),
		@ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
	public ResponseEntity create(@RequestBody final CreateMovieRequest movie) {
		final Movie created = movieService.validateAndCreate(movie);
		return ResponseEntity.created(URI.create(String.format("/v1/movies/%s", created.getExternalId()))).build();
	}

	@GetMapping("/{externalId}")
	@Operation(
		summary = "Retrieve a Movie by External ID",
		description = "Retrieve a specific Movie, using the External ID on the path. It will returns a JSON with all data of the Movie (title, producers and studios).")
	@ApiResponses({
		@ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = MovieResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE) }),
		@ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(), mediaType = MediaType.TEXT_PLAIN_VALUE) }),
		@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
	})
	public ResponseEntity<MovieResponse> findByExternalId(@PathVariable final UUID externalId) {
		final var movie = movieService.findByExternalId(externalId);
		return ResponseEntity.ok(MovieResponseBuilder.buildWithhAllData(movie));
	}

	@GetMapping
	@Operation(summary = "Retrieve all Movies")
	@ApiResponses({
		@ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = PageResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE) }),
		@ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
		@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
	})
	public ResponseEntity findAll(
		@RequestParam(value = "page", required = false, defaultValue = "1") final int page,
		@RequestParam(value = "size", required = false, defaultValue = "10") final int size) {
		movieService.validatePageSize(size);
		final var movie = movieService.findAll(PageRequest.of((page - 1), size, Sort.Direction.ASC, "title"))
			.map(MovieResponseBuilder::buildWithhAllData);
		return ResponseEntity.ok(new PageResponse(movie.getContent(), page, size, movie.getTotalPages(), movie.getTotalElements()));
	}

	@PutMapping("/{externalId}")
	@Operation(summary = "Update a Movie by External ID")
	@ApiResponses({
		@ApiResponse(responseCode = "204", content = { @Content(schema = @Schema()) }),
		@ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
		@ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
		@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
	})
	public ResponseEntity update(@PathVariable final UUID externalId, @RequestBody final UpdateNameRequest updateRequest) {
		movieService.updateTitle(externalId, updateRequest);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@DeleteMapping("/{externalId}")
	@Operation(summary = "Delete a Movie by External ID")
	@ApiResponses({
		@ApiResponse(responseCode = "204", content = { @Content(schema = @Schema()) }),
		@ApiResponse(responseCode = "404", content = { @Content(examples = {@ExampleObject(summary = "Movie not found", value = "Movie not found with ExternalId XYZ")}, schema = @Schema()) }),
		@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
	})
	public ResponseEntity delete(@PathVariable final UUID externalId) {
		movieService.delete(externalId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@GetMapping("/{externalId}/producers")
	@Operation(summary = "Retrieve all Producers of the Movie")
	@ApiResponses({
		@ApiResponse(responseCode = "200", content = { @Content(array = @ArraySchema(schema = @Schema(implementation = ProducerResponse.class)), mediaType = MediaType.APPLICATION_JSON_VALUE) }),
		@ApiResponse(responseCode = "204", content = { @Content(schema = @Schema()) }),
		@ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
		@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema) })
	})
	public ResponseEntity<List<ProducerResponse>> findAllProducers(@PathVariable final UUID externalId) {
		final var movieProducers = movieService.findAllProducers(externalId);
		if (movieProducers.isPresent())
			return ResponseEntity.ok(ProducerResponseBuilder.build(movieProducers.get()));
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@GetMapping("/{externalId}/studios")
	@Operation(summary = "Retrieve all Studios of the Movie")
	@ApiResponses({
		@ApiResponse(responseCode = "200", content = { @Content(array = @ArraySchema(schema = @Schema(implementation = StudioResponse.class)), mediaType = MediaType.APPLICATION_JSON_VALUE) }),
		@ApiResponse(responseCode = "204", content = { @Content(schema = @Schema()) }),
		@ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
		@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema) })
	})
	public ResponseEntity<List<StudioResponse>> findAllStudios(@PathVariable final UUID externalId) {
		final var movieStudios = movieService.findAllStudios(externalId);
		if (movieStudios.isPresent())
			return ResponseEntity.ok(StudioResponseBuilder.build(movieStudios.get()));
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
