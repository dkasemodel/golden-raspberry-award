package com.kasemodel.goldenraspberryaward.interfaces.rest.model.mapper;

import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Studio;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.StudioResponse;

import java.util.List;
import java.util.stream.Collectors;

public final class StudioResponseBuilder {
	private StudioResponseBuilder() {
		throw new UnsupportedOperationException("Utility class cannot be instantiated");
	}

	public static StudioResponse build(final Studio studio) {
		return new StudioResponse(studio.getExternalId(), studio.getName());
	}

	public static List<StudioResponse> build(List<Studio> studios) {
		return studios.stream().map(StudioResponseBuilder::build)
			.collect(Collectors.toList());
	}
}
