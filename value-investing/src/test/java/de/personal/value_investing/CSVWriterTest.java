package de.personal.value_investing;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.personal.value_investing.database.Database;
import de.personal.value_investing.utils.CSVReader;
import de.personal.value_investing.utils.CSVWriter;

public class CSVWriterTest {
	
	private Database data;
	
	@BeforeEach
	public void setup() {
		System.out.println("CSVWriter Test starts");
		this.data = new Database("AAPL", "XNAS", CSVReader.readMorningstarDataFromCSV("AAPL_MData_Test.csv"));
	}

	@Test
	@DisplayName("Write Morningstar Data to CSV file")
	public void testWriteMorningstarDataToCSV() {
		CSVWriter.writeMorningstarDataToCSV(this.data.getData(), "AAPL_MData_Test_" + LocalDate.now() + ".csv");
	}
}
