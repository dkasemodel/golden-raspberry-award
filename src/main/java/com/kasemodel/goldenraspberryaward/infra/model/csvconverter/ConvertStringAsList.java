package com.kasemodel.goldenraspberryaward.infra.model.csvconverter;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class ConvertStringAsList extends AbstractBeanField {
	@Override
	protected Object convert(final String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
		final String[] values = value.split(",+| and +");
		final var result = Stream.of(values).filter(StringUtils::isNotBlank).collect(Collectors.toList());
		if (log.isDebugEnabled()) {
			log.debug("'{}' Converted into {} items", value, result.size());
		}
		return result;
	}
}
