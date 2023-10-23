package com.kasemodel.goldenraspberryaward.interfaces.rest.controller;

import com.kasemodel.goldenraspberryaward.application.ProducerService;
import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Producer;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.CreateByNameRequest;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.PageResponse;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.ProducerResponse;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.UpdateNameRequest;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.mapper.ProducerResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/v1/producers")
@RequiredArgsConstructor
@Slf4j
public class ProducerController {
	private final ProducerService producerService;

	@PostMapping
	@Operation(
		summary = "Create new Producer",
		description = "Create new Producer with the name passed as parameter. The method will return a status 201 (created) if everything is OK",
		tags = {"Producers", "post"}
	)
	@ApiResponses({
		@ApiResponse(responseCode = "201", content = {@Content(schema = @Schema())}),
		@ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())}),
		@ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
	public ResponseEntity create(@RequestBody final CreateByNameRequest producer) {
		final Producer created = producerService.validateAndCreate(producer);
		return ResponseEntity.created(URI.create(String.format("/v1/producers/%s", created.getExternalId()))).build();
	}

	@GetMapping("/{externalId}")
	@Operation(
		summary = "Retrieve a Producer by External ID",
		description = "Retrieve a specific Producer, using the External ID on the path. It will returns a JSON with external_id and name.",
		tags = {"Producers", "get"}
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = ProducerResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE) }),
		@ApiResponse(responseCode = "404", content = { @Content(schema = @Schema(), mediaType = MediaType.TEXT_PLAIN_VALUE) }),
		@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
	})
	public ResponseEntity<ProducerResponse> findByExternalId(@PathVariable final UUID externalId) {
		final Optional<Producer> optProducer = producerService.findByExternalId(externalId);
		if (optProducer.isPresent()) {
			return ResponseEntity.ok(ProducerResponseBuilder.build(optProducer.get()));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@GetMapping
	@Operation(
		summary = "Retrieve all Producers",
		tags = {"Producers", "get"}
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = PageResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE) }),
		@ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
		@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
	})
	public ResponseEntity<PageResponse> findAll(
		@RequestParam(value = "page", required = false, defaultValue = "1") final int page,
		@RequestParam(value = "size", required = false, defaultValue = "10") final int size) {
		producerService.validatePageSize(size);
		Page<ProducerResponse> producersPage = producerService.findAll(PageRequest.of((page - 1), size, Sort.Direction.ASC, "name"))
			.map(ProducerResponseBuilder::build);
		return ResponseEntity.ok(new PageResponse(producersPage.getContent(), page, size, producersPage.getTotalPages(), producersPage.getTotalElements()));
	}

	@PutMapping("/{externalId}")
	@Operation(
		summary = "Update a Producer by External ID",
		tags = { "Studios", "put" }
	)
	@ApiResponses({
		@ApiResponse(responseCode = "204", content = { @Content(schema = @Schema()) }),
		@ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
		@ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
		@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
	})
	public ResponseEntity update(@PathVariable final UUID externalId, @RequestBody final UpdateNameRequest updateRequest) {
		producerService.updateName(externalId, updateRequest.name());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@DeleteMapping("/{externalId}")
	@Operation(
		summary = "Delete a Producer by External ID",
		tags = { "Producers", "delete" }
	)
	@ApiResponses({
		@ApiResponse(responseCode = "204", content = { @Content(schema = @Schema()) }),
		@ApiResponse(responseCode = "404", content = { @Content(examples = {@ExampleObject(summary = "Producer not found", value = "Producer not found with ExternalId XYZ")}, schema = @Schema()) }),
		@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
	})
	public ResponseEntity delete(@PathVariable final UUID externalId) {
		producerService.delete(externalId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
