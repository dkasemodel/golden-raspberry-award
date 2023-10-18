package com.kasemodel.goldenraspberryaward.application.impl;

import com.kasemodel.goldenraspberryaward.application.ImporterServie;
import com.kasemodel.goldenraspberryaward.application.InitializeData;
import com.kasemodel.goldenraspberryaward.infra.model.InitialDataVO;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class InitializeDataImpl implements InitializeData {
	private final ImporterServie importerServie;

	@Value("${award.initial-data.file}")
	private String dataFile;

	@EventListener(ApplicationReadyEvent.class)
	@Override
	public void execute() {
		try {
			validateFile(dataFile);
			loadData();
		} catch (final Exception e) {
			log.error("can not init data", e);
			System.exit(1);
		}
	}

	private void validateFile(final String dataFile) {
		if (StringUtils.isBlank(dataFile))
			throw new IllegalArgumentException("award.initial-data.file can not be null");
		if (!dataFile.endsWith(".csv"))
			throw new IllegalArgumentException(String.format("%s needs to be a CSV file", dataFile));
		isFileExists(dataFile);
	}

	private void isFileExists(final String dataFile) {
		final File file = new File(dataFile);
		if (!file.exists())
			throw new IllegalArgumentException(String.format("%s file does not exists", dataFile));
	}

	private void loadData() throws FileNotFoundException {
		final List<InitialDataVO> data = getDataFromCsv();
		if (CollectionUtils.isEmpty(data))
			throw new IllegalArgumentException(String.format("no data found inside %s file", dataFile));
		data.stream().forEach(importerServie::importData);
		log.info("Initial data loaded! CSV file: {}", dataFile);
	}

	private List<InitialDataVO> getDataFromCsv() throws FileNotFoundException {
		final List<InitialDataVO> data = new CsvToBeanBuilder(new FileReader(dataFile))
			.withType(InitialDataVO.class)
			.withSeparator(';')
			.build()
			.parse();
		return data;
	}
}
