package com.kasemodel.goldenraspberryaward.interfaces.rest.controller;

import com.kasemodel.goldenraspberryaward.application.StudioService;
import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Studio;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.CreateByNameRequest;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.PageResponse;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.StudioResponse;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.UpdateNameRequest;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.mapper.StudioResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController()
@RequestMapping("/v1/studios")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Studios", description = "Studios Controller")
public class StudioController {
	private final StudioService studioService;

	@PostMapping
	@Operation(
		summary = "Create new Studio",
		description = "Create new Studio with the name passed as parameter. The method will return a status 201 (created) if everything is OK")
	@ApiResponses({
		@ApiResponse(responseCode = "201", content = { @Content(schema = @Schema()) }),
		@ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
		@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
	})
	public ResponseEntity create(@RequestBody final CreateByNameRequest studio) {
		final Studio created = studioService.validateAndCreate(studio.name());
		return ResponseEntity.created(URI.create(String.format("/v1/studios/%s", created.getExternalId()))).build();
	}

	@GetMapping("/{externalId}")
	@Operation(
		summary = "Retrieve a Studio by External ID",
		description = "Retrieve a specific Studio, using the External ID on the path. It will returns a JSON with external_id and name.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = StudioResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE) }),
		@ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(), mediaType = MediaType.TEXT_PLAIN_VALUE) }),
		@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
	})
	public ResponseEntity<StudioResponse> findByExternalId(@PathVariable final UUID externalId) {
		final Studio studio = studioService.findByExternalId(externalId);
		return ResponseEntity.ok(StudioResponseBuilder.build(studio));
	}

	@GetMapping
	@Operation(summary = "Retrieve all Studios")
	@ApiResponses({
		@ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = PageResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE) }),
		@ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
		@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
	})
	public ResponseEntity<PageResponse> findAll(
		@RequestParam(value = "page", required = false, defaultValue = "1") final int page,
		@RequestParam(value = "size", required = false, defaultValue = "10") final int size) {
		studioService.validatePageSize(size);
		Page<StudioResponse> studiosPage = studioService.findAll(PageRequest.of((page - 1), size, Sort.Direction.ASC, "name"))
			.map(StudioResponseBuilder::build);
		return ResponseEntity.ok(new PageResponse(studiosPage.getContent(), page, size, studiosPage.getTotalPages(), studiosPage.getTotalElements()));
	}

	@PutMapping("/{externalId}")
	@Operation(summary = "Update a Studio by External ID")
	@ApiResponses({
		@ApiResponse(responseCode = "204", content = { @Content(schema = @Schema()) }),
		@ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
		@ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
		@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
	})
	public ResponseEntity update(@PathVariable final UUID externalId, @RequestBody final UpdateNameRequest updateRequest) {
		studioService.updateName(externalId, updateRequest.name());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@DeleteMapping("/{externalId}")
	@Operation(summary = "Delete a Studio by External ID")
	@ApiResponses({
		@ApiResponse(responseCode = "204", content = { @Content(schema = @Schema()) }),
		@ApiResponse(responseCode = "404", content = { @Content(examples = {@ExampleObject(summary = "Studio not found", value = "Studio not found with ExternalId XYZ")}, schema = @Schema()) }),
		@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
	})
	public ResponseEntity delete(@PathVariable final UUID externalId) {
		studioService.delete(externalId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
