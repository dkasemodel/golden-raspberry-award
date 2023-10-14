package com.kasemodel.goldenraspberryaward.infra.model;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class ConvertStringAsList extends AbstractBeanField {
	@Override
	protected Object convert(final String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
		log.info(value);
		final String[] values = value.split(",+| and +");
		final var result = Stream.of(values).filter(StringUtils::isNotBlank).collect(Collectors.toList());
		result.stream().forEach(log::info);
		return result;
	}
}
