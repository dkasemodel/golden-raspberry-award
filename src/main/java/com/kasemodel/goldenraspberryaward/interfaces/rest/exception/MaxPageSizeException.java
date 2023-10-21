package com.kasemodel.goldenraspberryaward.interfaces.rest.exception;

import com.kasemodel.goldenraspberryaward.interfaces.rest.util.PageableValuesEnum;

//@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class MaxPageSizeException extends RuntimeException {
	public MaxPageSizeException(final int size) {
		super(String.format("Page size cannot be greater than %d. Actual: %d", PageableValuesEnum.MAX_PAGE_SIZE.getValue(), size));
	}
}
