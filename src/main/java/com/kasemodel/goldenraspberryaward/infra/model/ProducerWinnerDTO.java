package com.kasemodel.goldenraspberryaward.infra.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProducerWinnerDTO implements Serializable, Comparable<ProducerWinnerDTO> {
	private String producer;
	private Integer interval;
	private Integer previousWin;
	private Integer followingWin;
	@Getter(AccessLevel.NONE)
	private int hashCode;

	private ProducerWinnerDTO(final String producer, final Integer interval, final Integer previousWin, final Integer followingWin) {
		validateYears(previousWin, followingWin);
		this.producer = producer;
		this.interval = interval;
		this.previousWin = previousWin;
		this.followingWin = followingWin;
		generateAndSetHashCode();
	}

	public static ProducerWinnerDTO of(String producer, Integer interval, Integer previousYear, Integer followingYear) {
		return new ProducerWinnerDTO(producer, interval, previousYear, followingYear);
	}

	@Override
	public int hashCode() {
		return hashCode;
	}

	@Override
	public int compareTo(ProducerWinnerDTO other) {
		return Integer.compare(this.previousWin, other.getPreviousWin());
	}

	private void validateYears(final Integer previous, final Integer following) {
		if (previous > following)
			throw new IllegalArgumentException("previous win is bigger than following win");
	}

	private void generateAndSetHashCode() {
		this.hashCode = Objects.hash(this.producer, this.interval, this.previousWin, this.followingWin);
	}
}
