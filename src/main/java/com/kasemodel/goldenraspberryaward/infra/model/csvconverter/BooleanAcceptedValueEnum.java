package com.kasemodel.goldenraspberryaward.infra.model.csvconverter;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

@Slf4j
public enum BooleanAcceptedValueEnum {
	NO(false),
	YES(true);

	@Getter
	private Boolean value;

	BooleanAcceptedValueEnum(boolean value) {
		this.value = value;
	}

	public static Optional<BooleanAcceptedValueEnum> nullableValueOf(final String value) {
		try {
			return Optional.ofNullable(BooleanAcceptedValueEnum.valueOf(StringUtils.upperCase(value)));
		} catch (final IllegalArgumentException e) {
			log.warn("{} is not a valid value for a boolean column", value);
			return Optional.empty();
		}
	}
}
