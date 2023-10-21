package com.kasemodel.goldenraspberryaward.interfaces.rest.util;

import lombok.Getter;

public enum PageableValuesEnum {
	MAX_PAGE_SIZE(50);

	PageableValuesEnum(final int value) {
		this.value = value;
	}

	@Getter
	private int value;
}
