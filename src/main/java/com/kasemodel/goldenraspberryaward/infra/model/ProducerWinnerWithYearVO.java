package com.kasemodel.goldenraspberryaward.infra.model;

import lombok.AccessLevel;
import lombok.Getter;

import java.util.Objects;
import java.util.TreeSet;

@Getter
public final class ProducerWinnerWithYearVO {
	private final String producer;
	private TreeSet<Integer> years;
	private TreeSet<IntervalVO> yearsGaps;
	private Integer previousYear;
	@Getter(AccessLevel.NONE)
	private int hashCode;

	private ProducerWinnerWithYearVO(final String producer, final Integer year) {
		this.producer = producer;
		this.years = new TreeSet<>();
		this.years.add(year);
		this.yearsGaps = new TreeSet<>();
		this.previousYear = year;
		calculateAndSetHashCode();
	}

	public static ProducerWinnerWithYearVO of(final String producer, final Integer year) {
		return new ProducerWinnerWithYearVO(producer, year);
	}

	@Override
	public int hashCode() {
		return this.hashCode;
	}

	public void addYearAndCalculateGap(final Integer year) {
		this.years.add(year);
	}

	public TreeSet<IntervalVO> calculateIntervals() {
		if (!this.yearsGaps.isEmpty())
			this.yearsGaps.clear();
		int previousYear = 0;
		final var yearsIterator = this.years.iterator();
		while (yearsIterator.hasNext()) {
			final int currentYear = yearsIterator.next();
			if (previousYear > 0)
				this.yearsGaps.add(IntervalVO.of((currentYear - previousYear), previousYear, currentYear));
			previousYear = currentYear;
		}
		calculateAndSetHashCode();
		return yearsGaps;
	}

	private void calculateAndSetHashCode() {
		this.hashCode = Objects.hash(producer, years, yearsGaps, previousYear, yearsGaps);
	}
}
