package com.kasemodel.goldenraspberryaward;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileNotFoundException;
import java.io.FileReader;

@SpringBootApplication
@Slf4j
public class GoldenRaspberryAwardApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(GoldenRaspberryAwardApplication.class, args);
	}
}
