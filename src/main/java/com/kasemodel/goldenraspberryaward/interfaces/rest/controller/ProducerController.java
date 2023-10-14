package com.kasemodel.goldenraspberryaward.interfaces.rest.controller;

import com.kasemodel.goldenraspberryaward.application.ProducerService;
import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Producer;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.GetProducerResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/v1/producer")
@RequiredArgsConstructor
@Slf4j
public class ProducerController {
	private final ProducerService producerService;

	@GetMapping(
		value = "/{externalId}",
		consumes = MediaType.APPLICATION_JSON_VALUE,
		produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GetProducerResponse> get(@RequestParam final UUID externalId) {
//		final Optional<Producer> optProducer = producerService.findByExternalId(externalId);
//		if (optProducer.isPresent()) {
//			final Producer producer = optProducer.get();
////			return ResponseEntity.ok(GetProducerResponse.of(producer.getExternalId(), producer.getName()));
//			return null;
//		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
}
