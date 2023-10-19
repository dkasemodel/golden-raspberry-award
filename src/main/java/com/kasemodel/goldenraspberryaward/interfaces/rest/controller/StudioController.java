package com.kasemodel.goldenraspberryaward.interfaces.rest.controller;

import com.kasemodel.goldenraspberryaward.application.StudioService;
import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Studio;
import com.kasemodel.goldenraspberryaward.infra.persistence.exception.StudioNotFoundException;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.CreateByNameRequest;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.StudioResponse;
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
	value = "/v1/studios",
	consumes = MediaType.APPLICATION_JSON_VALUE,
	produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class StudioController {
	private final StudioService studioService;

	@PostMapping
	public ResponseEntity create(@RequestBody final CreateByNameRequest studio) {
		if (studio == null || StringUtils.isBlank(studio.name()))
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("studio name is required");
		final Studio created = studioService.validateAndCreate(studio.name());
		return ResponseEntity.created(URI.create(String.format("/v1/producers/%s", created.getExternalId()))).build();
	}

	@GetMapping("/{externalId}")
	public ResponseEntity<StudioResponse> findByExternalId(@PathVariable final UUID externalId) {
		final Optional<Studio> optStudio = studioService.findByExternalId(externalId);
		if (optStudio.isPresent()) {
			final Studio studio = optStudio.get();
			return ResponseEntity.ok(new StudioResponse(studio.getExternalId(), studio.getName()));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@GetMapping
	public ResponseEntity<List<StudioResponse>> findAll() {
		final var studios = studioService.findAll();
		if (CollectionUtils.isEmpty(studios)) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		return ResponseEntity.ok(studios);
	}

	@PutMapping("/{externalId}")
	public ResponseEntity update(@PathVariable final UUID externalId, @RequestBody final UpdateNameRequest updateRequest) {
		try {
			studioService.updateName(externalId, updateRequest.name());
		} catch (StudioNotFoundException e) {
			log.error("Error when try to update Studio", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@DeleteMapping("/{externalId}")
	public ResponseEntity delete(@PathVariable final UUID externalId) {
		try {
			studioService.delete(externalId);
		} catch (final Exception e) {
			log.error("Error when deleting producer", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
