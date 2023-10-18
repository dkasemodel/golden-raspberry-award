package com.kasemodel.goldenraspberryaward.infra.model;

import com.kasemodel.goldenraspberryaward.infra.model.csvconverter.ConvertStringAsBoolean;
import com.kasemodel.goldenraspberryaward.infra.model.csvconverter.ConvertStringAsList;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class InitialDataVO {
	@CsvBindByName(required = true)
	private Short year;
	@CsvBindByName(required = true)
	private String title;
	@CsvCustomBindByName(required = true, converter = ConvertStringAsList.class)
	private List<String> studios;
	@CsvCustomBindByName(required = true, converter = ConvertStringAsList.class)
	private List<String> producers;
	@CsvCustomBindByName(converter = ConvertStringAsBoolean.class)
	private Boolean winner;
}
