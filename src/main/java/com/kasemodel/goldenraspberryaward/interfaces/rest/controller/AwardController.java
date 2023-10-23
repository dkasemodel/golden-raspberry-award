package com.kasemodel.goldenraspberryaward.interfaces.rest.controller;

import com.kasemodel.goldenraspberryaward.application.AwardService;
import com.kasemodel.goldenraspberryaward.application.ProcessWinnersService;
import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Award;
import com.kasemodel.goldenraspberryaward.interfaces.rest.exception.MaxPageSizeException;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.MovieResponse;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.PageResponse;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.award.AwardCreateRequest;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.award.AwardResponse;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.award.AwardWinnersResponse;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.award.UpdateAwardRequest;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.mapper.AwardResponseBuilder;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.mapper.MovieResponseBuilder;
import com.kasemodel.goldenraspberryaward.interfaces.rest.util.PageableValuesEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
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
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/v1/awards")
@RequiredArgsConstructor
@Tag(name = "Awards", description = "Award Controller")
@Slf4j
public class AwardController {
	private final ProcessWinnersService processWinnersService;
	private final AwardService awardService;

	@GetMapping("/producers/winners")
	@Operation(
		summary = "Retrieve the Producers winners with minimum and maximum interval",
		description = "Get the lists of winners with minimum interval and maximum interval between the awards. The response contains two lists, min (with the minimum interval between two awards) and manx (with the maximum interval between two awards)")
	@ApiResponses({
		@ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = AwardWinnersResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE) }),
		@ApiResponse(responseCode = "204", content = { @Content(schema = @Schema()) }),
		@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
	})
	public ResponseEntity<AwardWinnersResponse> processWinners() {
		final var result = processWinnersService.processWinners();
		if (result == null) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		return ResponseEntity.ok().body(new AwardWinnersResponse(result.getLeft(), result.getRight()));
	}

	@PostMapping
	@Operation(
		summary = "Create new Award",
		description = "Create new Award with the sent by body. The method will return a status 201 (created) if everything is OK")
	@ApiResponses({
		@ApiResponse(responseCode = "201", content = {@Content(schema = @Schema())}),
		@ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())}),
		@ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
	public ResponseEntity create(@RequestBody final AwardCreateRequest awardCreateRequest) {
		final var award = awardService.create(awardCreateRequest);
		return ResponseEntity.created(URI.create(String.format("/v1/awards/%s", award.getExternalId()))).build();
	}

	@GetMapping("/{externalId}")
	@Operation(summary = "Retrieve Award by External ID")
	@ApiResponses({
		@ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = AwardResponse.class), mediaType = "application/json")}),
		@ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())}),
		@ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
		@ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
	public ResponseEntity<AwardResponse> findByExternalId(@PathVariable final UUID externalId) {
		final Optional<Award> award = awardService.findFetchByExternalId(externalId);
		if (award.isPresent()) {
			return ResponseEntity.ok(AwardResponseBuilder.buildWithAllData(award.get()));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@GetMapping
	@Operation(summary = "Retrieve all Awards paginated")
	@ApiResponses({
		@ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = PageResponse.class), mediaType = "application/json")}),
		@ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())}),
		@ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
	public ResponseEntity findAll(
		@RequestParam(value = "page", required = false, defaultValue = "1") final int page,
		@RequestParam(value = "size", required = false, defaultValue = "10") final int size) {
		if (size > PageableValuesEnum.MAX_PAGE_SIZE.getValue())
			throw new MaxPageSizeException(size);
		final var award = awardService.findAll(PageRequest.of((page - 1), size, Sort.Direction.ASC, "year"))
			.map(AwardResponseBuilder::buildWithMinimumData);
		return ResponseEntity.ok(new PageResponse(award.getContent(), page, size, award.getTotalPages(), award.getTotalElements()));
	}

	@PutMapping("/{externalId}")
	@Operation(
		summary = "Update an Award by External ID",
		description = "Update Year and Winner by External ID. Year must contains 4 digits! Winner is not necessary, if it's NULL than it'll be FALSE")
	@ApiResponses({
		@ApiResponse(responseCode = "204", content = {@Content(schema = @Schema())}),
		@ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())}),
		@ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
	public ResponseEntity update(@PathVariable final UUID externalId, @RequestBody final UpdateAwardRequest updateRequest) {
		awardService.update(externalId, updateRequest);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@DeleteMapping("/{externalId}")
	@Operation(
		summary = "Delete an Award by External ID",
		description = "Soft delete. Set the current Date/Time value into the DeletedAt property")
	@ApiResponses({
		@ApiResponse(responseCode = "204", content = {@Content(schema = @Schema())}),
		@ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())}),
		@ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})
	})
	public ResponseEntity delete(@PathVariable final UUID externalId) {
		awardService.delete(externalId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@GetMapping("/{externalId}/movie")
	@Operation(summary = "Retrieve the movie of the Award")
	@ApiResponses({
		@ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = MovieResponse.class, type = "application/json"))}),
		@ApiResponse(responseCode = "204", content = {@Content(schema = @Schema())}),
		@ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())}),
		@ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})
	})
	public ResponseEntity<MovieResponse> findMovie(@PathVariable final UUID externalId) {
		final var awardMovie = awardService.findMovie(externalId);
		if (awardMovie.isPresent())
			return ResponseEntity.ok(MovieResponseBuilder.buildWithhAllData(awardMovie.get()));
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
