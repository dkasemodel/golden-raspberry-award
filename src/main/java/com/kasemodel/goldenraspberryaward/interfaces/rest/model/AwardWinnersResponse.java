package com.kasemodel.goldenraspberryaward.interfaces.rest.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kasemodel.goldenraspberryaward.infra.model.ProducerWinnerDTO;
import lombok.Getter;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Getter
public class AwardWinnersResponse {
	private List<ProducerWinnerDTO> min;
	private List<ProducerWinnerDTO> max;

	private AwardWinnersResponse(final List<ProducerWinnerDTO> min, final List<ProducerWinnerDTO> max) {
		this.min = min;
		this.max = max;
	}

	public static AwardWinnersResponse of(final List<ProducerWinnerDTO> min, final List<ProducerWinnerDTO> max) {
		return new AwardWinnersResponse(min, max);
	}
}
