package com.kasemodel.goldenraspberryaward.interfaces.rest.controller;

import com.kasemodel.goldenraspberryaward.application.ProducerService;
import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Producer;
import com.kasemodel.goldenraspberryaward.infra.persistence.exception.ProducerNotFoundException;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.CreateProducerRequest;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.ProducerResponse;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.UpdateProducerRequest;
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
@RequestMapping("/v1/producers")
@RequiredArgsConstructor
@Slf4j
public class ProducerController {
	private final ProducerService producerService;

	@PostMapping
	public ResponseEntity create(@RequestBody final CreateProducerRequest producer) {
		if (producer == null || StringUtils.isBlank(producer.getName()))
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("producer name is required");
		final Producer created = producerService.validateAndCreate(producer.getName());
		return ResponseEntity.created(URI.create(String.format("/v1/producers/%s", created.getExternalId()))).build();
	}

	@GetMapping(
		value = "/{externalId}",
		consumes = MediaType.APPLICATION_JSON_VALUE,
		produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProducerResponse> findByExternalId(@PathVariable final UUID externalId) {
		final Optional<Producer> optProducer = producerService.findByExternalId(externalId);
		if (optProducer.isPresent()) {
			final Producer producer = optProducer.get();
			return ResponseEntity.ok(new ProducerResponse(producer.getExternalId(), producer.getName()));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@GetMapping(
		consumes = MediaType.APPLICATION_JSON_VALUE,
		produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ProducerResponse>> findAll() {
		final var producers = producerService.findAll();
		if (CollectionUtils.isEmpty(producers)) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		return ResponseEntity.ok(producers);
	}

	@PutMapping(
		value = "/{externalId}",
		consumes = MediaType.APPLICATION_JSON_VALUE,
		produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity update(@PathVariable final UUID externalId, @RequestBody final UpdateProducerRequest updateRequest) {
		try {
			producerService.updateName(externalId, updateRequest.name());
		} catch (ProducerNotFoundException e) {
			log.error("Error when try to update Producer", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
