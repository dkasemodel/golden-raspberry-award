package com.kasemodel.goldenraspberryaward.infra.model.csvconverter;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import org.apache.commons.lang3.StringUtils;

public class ConvertStringAsBoolean extends AbstractBeanField {
	@Override
	protected Object convert(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
		if (StringUtils.isBlank(value)) {
			return false;
		}
		return BooleanAcceptedValueEnum.nullableValueOf(value)
			.map(element -> (Object) element.getValue())
			.orElseThrow(() -> new CsvDataTypeMismatchException(String.format("%s is not valid for a boolean column", value)));
	}
}
