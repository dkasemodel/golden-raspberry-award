package com.kasemodel.goldenraspberryaward.interfaces.rest.controller;

import com.kasemodel.goldenraspberryaward.application.AwardService;
import com.kasemodel.goldenraspberryaward.application.ProcessWinnersService;
import com.kasemodel.goldenraspberryaward.infra.model.ProducerWinnerDTO;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.AwardWinnersResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(
	value = "/v1/award",
	consumes = MediaType.APPLICATION_JSON_VALUE,
	produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class AwardController {
	private final ProcessWinnersService processWinnersService;

	@PostMapping("/winners")
	public ResponseEntity<AwardWinnersResponse> processWinners() {
		final var result = processWinnersService.processWinners();
		if (result == null) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		return ResponseEntity.ok(AwardWinnersResponse.of(result.getLeft(), result.getRight()));
	}
}
