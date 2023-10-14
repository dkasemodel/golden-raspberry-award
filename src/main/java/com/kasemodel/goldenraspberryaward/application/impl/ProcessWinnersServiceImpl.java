package com.kasemodel.goldenraspberryaward.application.impl;

import com.kasemodel.goldenraspberryaward.application.ProcessWinnersService;
import com.kasemodel.goldenraspberryaward.application.ProducerService;
import com.kasemodel.goldenraspberryaward.infra.model.ProducerWinnerDTO;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProcessWinnersServiceImpl implements ProcessWinnersService {
	private static final int PRODUCER_NAME_INDEX = 0;
	private static final int PRODUCER_YEAR_INDEX = 1;

	private final ProducerService producerService;

	@Override
	public ImmutablePair<List<ProducerWinnerDTO>, List<ProducerWinnerDTO>> processWinners() {
		final var optionalWinners = producerService.findWinners();
		if (optionalWinners.isPresent()) {
			return calculateWinners(optionalWinners);
		}
		return null;
	}

	private ImmutablePair<List<ProducerWinnerDTO>, List<ProducerWinnerDTO>> calculateWinners(Optional<List<Tuple>> optionalWinners) {
		final var winners = optionalWinners.get();
		final Map<String, ProducerWinnerDTO> winnersMap = new HashMap<>();
		winners.stream().forEach(winner -> {
			final String name = winner.get(PRODUCER_NAME_INDEX, String.class);
			final short year = winner.get(PRODUCER_YEAR_INDEX, Short.class);
			if (!winnersMap.containsKey(name)) {
				winnersMap.put(name, ProducerWinnerDTO.of(name, year));
			} else {
				final ProducerWinnerDTO innerDTO = winnersMap.get(name);
				if (year < innerDTO.getPreviousWin())
					innerDTO.updatePreviousWin(Integer.valueOf(year));
				else if (year > innerDTO.getFollowingWin())
					innerDTO.updateFollowingWin(Integer.valueOf(year));
			}
		});
		return arrangeResult(winnersMap);
	}

	private ImmutablePair<List<ProducerWinnerDTO>, List<ProducerWinnerDTO>> arrangeResult(final Map<String, ProducerWinnerDTO> winnersMap) {
		final var minMaxIntervals = processMinMaxIntervals(winnersMap);
		final ImmutablePair<List<ProducerWinnerDTO>, List<ProducerWinnerDTO>> result = new ImmutablePair<>(new ArrayList<>(1), new ArrayList<>(1));
		winnersMap.values().stream().forEach(winner -> processWinner(minMaxIntervals, result, winner));
		return result;
	}

	private Pair<Integer, Integer> processMinMaxIntervals(Map<String, ProducerWinnerDTO> winnersMap) {
		final var resultList = winnersMap.values().stream()
			.collect(Collectors.toList());
		final var minWinner = resultList.stream()
			.min(this::sort)
			.get();
		final var maxWinner = resultList.stream()
			.max(this::sort)
			.get();
		return new ImmutablePair<>(minWinner.getInterval(), maxWinner.getInterval());
	}

	private void processWinner(final Pair<Integer, Integer> minMaxIntervals, final ImmutablePair<List<ProducerWinnerDTO>, List<ProducerWinnerDTO>> result, final ProducerWinnerDTO winner) {
		if (minMaxIntervals.getLeft().equals(winner.getInterval()))
			result.getLeft().add(winner);
		else if (minMaxIntervals.getRight().equals(winner.getInterval()))
			result.getRight().add(winner);
	}

	private static void processIntervals(MutablePair<Integer, Integer> minMaxIntervals, ProducerWinnerDTO innerDTO) {
		initializeIntervals(minMaxIntervals, innerDTO);
		if (minMaxIntervals.getLeft() > innerDTO.getInterval())
			minMaxIntervals.setLeft(innerDTO.getInterval());
		else if (minMaxIntervals.getRight() < innerDTO.getInterval())
			minMaxIntervals.setRight(innerDTO.getInterval());
	}

	private static void initializeIntervals(MutablePair<Integer, Integer> minMaxIntervals, ProducerWinnerDTO minWinner) {
		if (minMaxIntervals.getLeft() == 0)
			minMaxIntervals.setLeft(minWinner.getInterval());
	}

	private int sort(final ProducerWinnerDTO previous, final ProducerWinnerDTO next) {
		return previous.getInterval().equals(next.getInterval()) ? 0 :
			previous.getInterval() < next.getInterval() ? -1 : 1;
	}
}
