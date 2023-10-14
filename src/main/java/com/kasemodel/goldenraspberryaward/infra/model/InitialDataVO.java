package com.kasemodel.goldenraspberryaward.infra.model;

import com.opencsv.bean.CsvBindAndSplitByName;
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
	@CsvBindAndSplitByName(elementType = String.class, splitOn = ",+", writeDelimiter = ",")
	private List<String> studios;
//	@CsvBindAndSplitByName(elementType = String.class, splitOn = ",+|and")
	@CsvCustomBindByName(required = true, converter = ConvertStringAsList.class)
	private List<String> producers;
	@CsvBindByName
	private Boolean winner;
}
