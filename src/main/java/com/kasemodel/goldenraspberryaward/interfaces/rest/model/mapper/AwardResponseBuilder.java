package com.kasemodel.goldenraspberryaward.interfaces.rest.model.mapper;

import com.kasemodel.goldenraspberryaward.infra.persistence.entity.Award;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.award.AwardResponse;

public final class AwardResponseBuilder {
	private AwardResponseBuilder() {
		throw new UnsupportedOperationException("Utility class cannot be instantiated");
	}

	public static AwardResponse buildWithAllData(final Award award) {
		return new AwardResponse(
			award.getExternalId(),
			award.getYear(),
			award.getWinner(),
			MovieResponseBuilder.buildWithhAllData(award.getMovie()));
	}

	public static AwardResponse buildWithMinimumData(final Award award) {
		return new AwardResponse(
			award.getExternalId(),
			award.getYear(),
			award.getWinner(),
			MovieResponseBuilder.buildWithExternalIdAndTitle(award.getMovie()));
	}
}
