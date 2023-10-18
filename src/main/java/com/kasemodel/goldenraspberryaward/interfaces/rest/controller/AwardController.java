package com.kasemodel.goldenraspberryaward.interfaces.rest.controller;

import com.kasemodel.goldenraspberryaward.application.ProcessWinnersService;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.AwardWinnersResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
	value = "/v1/award",
	consumes = MediaType.APPLICATION_JSON_VALUE,
	produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class AwardController {
	private final ProcessWinnersService processWinnersService;

	@GetMapping("/winners")
	public ResponseEntity<AwardWinnersResponse> processWinners() {
		final var result = processWinnersService.processWinners();
		if (result == null) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		return ResponseEntity.ok().body(new AwardWinnersResponse(result.getLeft(), result.getRight()));
	}
}
