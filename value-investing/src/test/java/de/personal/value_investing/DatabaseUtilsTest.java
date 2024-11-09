package de.personal.value_investing;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.personal.value_investing.database.Database;
import de.personal.value_investing.utils.CSVWriter;

public class DatabaseUtilsTest {
	
	private Database data;
	
	@BeforeEach
	public void setup() {
		System.out.println("DatabaseUtils Test starts");
	}
	
	@Test
	@DisplayName("Build complete database.")
	public void testBuildCompleteDatabase() {
		this.data = new Database("AAPL", "XNAS", "0P000000GY");
		CSVWriter.writeMorningstarDataToCSV(this.data.getData(), "test_data_" + LocalDate.now() + ".csv");
		Assertions.assertEquals(24, this.data.getData().keySet().size());
	}
}