package com.kasemodel.goldenraspberryaward.interfaces.rest.model;

import lombok.Getter;

import java.util.UUID;

@Getter
public class GetProducerResponse {
	private UUID externalId;
	private String name;

	private GetProducerResponse(final UUID externalId, final String name) {
		this.externalId = externalId;
		this.name = name;
	}

	public static GetProducerResponse of(final UUID externalId, final String name) {
		return new GetProducerResponse(externalId, name);
	}
}
