package com.kasemodel.goldenraspberryaward.interfaces.rest.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Set;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record MovieResponse(UUID externalId, String title,
							Set<ProducerResponse> producers,
							Set<StudioResponse> studios) {
	public MovieResponse(UUID externalId, String title) {
		this(externalId, title, null, null);
	}
}
