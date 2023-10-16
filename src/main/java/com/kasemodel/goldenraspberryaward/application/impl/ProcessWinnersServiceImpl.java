package com.kasemodel.goldenraspberryaward.application.impl;

import com.kasemodel.goldenraspberryaward.application.ProcessWinnersService;
import com.kasemodel.goldenraspberryaward.application.ProducerService;
import com.kasemodel.goldenraspberryaward.infra.model.IntervalVO;
import com.kasemodel.goldenraspberryaward.infra.model.ProducerWinnerDTO;
import com.kasemodel.goldenraspberryaward.infra.model.ProducerWinnerWithYearVO;
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
			return calculateWinners(optionalWinners.get());
		}
		return null;
	}

	private ImmutablePair<List<ProducerWinnerDTO>, List<ProducerWinnerDTO>> calculateWinners(final List<Tuple> winners) {
		final Map<String, ProducerWinnerWithYearVO> winnersMap = processWinnersMap(winners);
		final Map<Integer, Set<ProducerWinnerDTO>> intervalsMap = new TreeMap<>();
		final ImmutablePair<Integer, Integer> minMaxIntervals = processMinMaxFinalListsPair(winnersMap, intervalsMap);
		return new ImmutablePair<>(
			intervalsMap.get(minMaxIntervals.getLeft()).stream().toList(),
			intervalsMap.get(minMaxIntervals.getRight()).stream().toList());

//		return new ImmutablePair<>(Collections.EMPTY_LIST, Collections.EMPTY_LIST);
	}

	private static ImmutablePair<Integer, Integer> processMinMaxFinalListsPair(Map<String, ProducerWinnerWithYearVO> winnersMap, Map<Integer, Set<ProducerWinnerDTO>> intervalsMap) {
		final MutablePair<Integer, Integer> minMaxIntervals = new MutablePair<>();
		final var finalWinnersIterator = winnersMap.values().iterator();
		while (finalWinnersIterator.hasNext()) {
			final var winner = finalWinnersIterator.next();
			final TreeSet<IntervalVO> intervals = winner.calculateIntervals();
			intervals.stream().forEach(interval -> {
				final Integer key = interval.getInterval();
				if (!intervalsMap.containsKey(key)) {
					intervalsMap.put(key, new TreeSet<>());
				}
				intervalsMap.get(key).add(ProducerWinnerDTO.of(winner.getProducer(), key, interval.getPreviousYear(), interval.getFollowingYear()));
				validateAndSetMinMaxIntervals(key, minMaxIntervals);
			});
		}
		if (minMaxIntervals.getLeft().equals(minMaxIntervals.getRight()))
			minMaxIntervals.setRight(null);
		return ImmutablePair.of(minMaxIntervals);
	}

	private static void validateAndSetMinMaxIntervals(Integer key, MutablePair<Integer, Integer> minMaxIntervals) {
		initilizeMinMaxIntervalsPair(minMaxIntervals, key);
		if (key < minMaxIntervals.getLeft())
			minMaxIntervals.setLeft(key);
		else if (key > minMaxIntervals.getRight())
			minMaxIntervals.setRight(key);
	}

	private static void initilizeMinMaxIntervalsPair(MutablePair<Integer, Integer> minMaxIntervals, Integer key) {
		if (minMaxIntervals.getLeft() == null)
			minMaxIntervals.setLeft(key);
		if (minMaxIntervals.getRight() == null)
			minMaxIntervals.setRight(key);
	}

	private static Map<String, ProducerWinnerWithYearVO> processWinnersMap(List<Tuple> winners) {
		final Map<String, ProducerWinnerWithYearVO> winnersMap = new HashMap<>();
		final var winnerIterator = winners.iterator();
		while (winnerIterator.hasNext()) {
			final var winner = winnerIterator.next();
			final String name = winner.get(PRODUCER_NAME_INDEX, String.class);
			final Integer year = Integer.valueOf(winner.get(PRODUCER_YEAR_INDEX, Short.class));
			if (!winnersMap.containsKey(name))
				winnersMap.put(name, ProducerWinnerWithYearVO.of(name, Integer.valueOf(year)));
			else
				winnersMap.get(name).addYearAndCalculateGap(year);
		}
		return winnersMap;
	}

	private int sortInt(final Integer previous, final Integer next) {
		return previous.equals(next) ? 0 :
			previous < next ? -1 : 1;
	}

	private void validateAndAddGapsMap(final Map<Integer, List<ProducerWinnerWithYearVO>> gapsMap, final int gap, final ProducerWinnerWithYearVO winnerVO) {
		if (!gapsMap.containsKey(gap))
			gapsMap.put(gap, new ArrayList<>());
		gapsMap.get(gap).add(winnerVO);
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
