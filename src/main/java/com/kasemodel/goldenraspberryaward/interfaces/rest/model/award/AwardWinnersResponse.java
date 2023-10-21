package com.kasemodel.goldenraspberryaward.interfaces.rest.model.award;

import com.kasemodel.goldenraspberryaward.infra.model.ProducerWinnerDTO;

import java.util.List;

public record AwardWinnersResponse(List<ProducerWinnerDTO> min,
								   List<ProducerWinnerDTO> max) {
}
