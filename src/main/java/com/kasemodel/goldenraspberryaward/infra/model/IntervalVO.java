package com.kasemodel.goldenraspberryaward.infra.model;

import lombok.Getter;

import java.util.Objects;

@Getter
public class IntervalVO implements Comparable<IntervalVO> {
	private final Integer interval;
	private final Integer previousYear;
	private final Integer followingYear;
	private final Integer hash;

	private IntervalVO(final Integer interval, final Integer previousYear, final Integer followingYear) {
		this.interval = interval;
		this.previousYear = previousYear;
		this.followingYear = followingYear;
		this.hash = Objects.hash(this.interval, this.previousYear, this.followingYear);
	}

	public static IntervalVO of(final Integer interval, final Integer previousYear, final Integer followingYear) {
		return new IntervalVO(interval, previousYear, followingYear);
	}

	@Override
	public int hashCode() {
		return this.hash;
	}

	@Override
	public int compareTo(final IntervalVO other) {
		return this.interval.compareTo(other.getInterval());
	}
}
