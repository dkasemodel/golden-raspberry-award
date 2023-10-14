package com.kasemodel.goldenraspberryaward.application;

import com.kasemodel.goldenraspberryaward.infra.model.ProducerWinnerDTO;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.List;

public interface ProcessWinnersService {
	ImmutablePair<List<ProducerWinnerDTO>, List<ProducerWinnerDTO>> processWinners();
}
