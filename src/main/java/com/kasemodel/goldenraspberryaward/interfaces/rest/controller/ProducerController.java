package com.kasemodel.goldenraspberryaward.interfaces.rest.controller;

import com.kasemodel.goldenraspberryaward.application.ProducerService;
import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Producer;
import com.kasemodel.goldenraspberryaward.infra.persistence.exception.ProducerNotFoundException;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.CreateByNameRequest;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.ProducerResponse;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.UpdateNameRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(
	value = "/v1/producers",
	consumes = MediaType.APPLICATION_JSON_VALUE,
	produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class ProducerController {
	private final ProducerService producerService;

	@PostMapping
	public ResponseEntity create(@RequestBody final CreateByNameRequest producer) {
		if (producer == null || StringUtils.isBlank(producer.name()))
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("producer name is required");
		final Producer created = producerService.validateAndCreate(producer.name());
		return ResponseEntity.created(URI.create(String.format("/v1/producers/%s", created.getExternalId()))).build();
	}

	@GetMapping("/{externalId}")
	public ResponseEntity<ProducerResponse> findByExternalId(@PathVariable final UUID externalId) {
		final Optional<Producer> optProducer = producerService.findByExternalId(externalId);
		if (optProducer.isPresent()) {
			final Producer producer = optProducer.get();
			return ResponseEntity.ok(new ProducerResponse(producer.getExternalId(), producer.getName()));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@GetMapping
	public ResponseEntity<List<ProducerResponse>> findAll() {
		final var producers = producerService.findAll();
		if (CollectionUtils.isEmpty(producers)) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		return ResponseEntity.ok(producers);
	}

	@PutMapping("/{externalId}")
	public ResponseEntity update(@PathVariable final UUID externalId, @RequestBody final UpdateNameRequest updateRequest) {
		try {
			producerService.updateName(externalId, updateRequest.name());
		} catch (ProducerNotFoundException e) {
			log.error("Error when try to update Producer", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@DeleteMapping("/{externalId}")
	public ResponseEntity delete(@PathVariable final UUID externalId) {
		try {
			producerService.delete(externalId);
		} catch (final Exception e) {
			log.error("Error when deleting producer", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
