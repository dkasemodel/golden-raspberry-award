package com.kasemodel.goldenraspberryaward.infra.model;

import lombok.AccessLevel;
import lombok.Getter;

import java.util.Objects;

@Getter
public class ProducerWinnerWithYearVO {
	private final String producer;
	private final Integer year;
	@Getter(AccessLevel.NONE)
	private final int hashCode;

	private ProducerWinnerWithYearVO(final String producer, final Integer year) {
		this.producer = producer;
		this.year = year;
		this.hashCode = Objects.hash(producer, year);
	}

	public static ProducerWinnerWithYearVO of(final String producer, final Integer year) {
		return new ProducerWinnerWithYearVO(producer, year);
	}

	@Override
	public int hashCode() {
		return this.hashCode;
	}
}
