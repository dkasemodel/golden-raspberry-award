package com.kasemodel.goldenraspberryaward.infra.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProducerWinnerDTO implements Serializable {
	private String producer;
	private Integer interval;
	private Integer previousWin;
	private Integer followingWin;
	@Getter(AccessLevel.NONE)
	private int hashCode;

	private ProducerWinnerDTO(final String producer, final Integer previousWin, final Integer followingWin) {
		validateYears(previousWin, followingWin);
		this.producer = producer;
		this.previousWin = previousWin;
		this.followingWin = followingWin;
		this.interval = calculateInterval();
		generateAndSetHashCode();
	}

	public static ProducerWinnerDTO of(String name, int year) {
		return new ProducerWinnerDTO(name, year, year);
	}

	public void updatePreviousWin(final Integer value) {
		validateYears(value, followingWin);
		previousWin = value;
		interval = calculateInterval();
		generateAndSetHashCode();
	}

	public void updateFollowingWin(final Integer value) {
		validateYears(previousWin, value);
		followingWin = value;
		interval = calculateInterval();
		generateAndSetHashCode();
	}

	@Override
	public int hashCode() {
		return hashCode;
	}

	private void validateYears(final Integer previous, final Integer following) {
		if (previous > following)
			throw new IllegalArgumentException("previous win is bigger than following win");
	}

	private int calculateInterval() {
		return followingWin - previousWin;
	}

	private void generateAndSetHashCode() {
		this.hashCode = Objects.hash(this.producer, this.interval, this.previousWin, this.followingWin);
	}
}
