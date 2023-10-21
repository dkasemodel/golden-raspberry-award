package com.kasemodel.goldenraspberryaward.interfaces.rest.model.award;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kasemodel.goldenraspberryaward.interfaces.rest.model.MovieResponse;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record AwardResponse(UUID externalId, Short year, boolean winner,
							MovieResponse movie) {
}
