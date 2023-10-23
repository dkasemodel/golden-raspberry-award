package com.kasemodel.goldenraspberryaward.application.impl;

import com.kasemodel.goldenraspberryaward.application.PageableService;
import com.kasemodel.goldenraspberryaward.interfaces.rest.exception.MaxPageSizeException;
import lombok.extern.slf4j.Slf4j;

import static com.kasemodel.goldenraspberryaward.interfaces.rest.util.PageableValuesEnum.MAX_PAGE_SIZE;

@Slf4j
public class PageableServiceImpl implements PageableService {
	@Override
	public void validatePageSize(int pageSize) {
		if (log.isDebugEnabled()) {
			log.debug("Validating page size. Provided: {}, MAXIMUM: {}", pageSize, MAX_PAGE_SIZE.getValue());
		}
		if (MAX_PAGE_SIZE.getValue() < pageSize)
			throw new MaxPageSizeException(pageSize);
	}
}
