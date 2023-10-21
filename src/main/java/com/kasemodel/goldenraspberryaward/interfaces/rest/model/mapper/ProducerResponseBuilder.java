package com.kasemodel.goldenraspberryaward.interfaces.rest.model.mapper;

import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Producer;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.ProducerResponse;

import java.util.List;
import java.util.stream.Collectors;

public final class ProducerResponseBuilder {
	private ProducerResponseBuilder() {
		throw new UnsupportedOperationException("Utility class cannot be instantiated");
	}

	public static ProducerResponse build(final Producer producer) {
		return new ProducerResponse(producer.getExternalId(), producer.getName());
	}

	public static List<ProducerResponse> build(List<Producer> producers) {
		return producers.stream().map(ProducerResponseBuilder::build)
			.collect(Collectors.toList());
	}
}
