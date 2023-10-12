package de.personal.value_investing;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.personal.value_investing.utils.CSVReader;
import de.personal.value_investing.utils.CompanyType;
import de.personal.value_investing.utils.Region;

public class CSVReaderTest {
	
	@BeforeEach
	public void setup() {
		System.out.println("CSVReader Test starts");
	}
	
	@Test
	@DisplayName("Read price data from CSV file")
	public void testReadPriceDataFromCSV() {
		Map<LocalDate, Double> stockPriceData = CSVReader.readPriceDataFromCSV("AAPL_Prices_Test.csv", true);
		List<Double> stockPrices = stockPriceData.values().stream().collect(Collectors.toList());

		Map<LocalDate, Double> benchmarkPriceData = CSVReader.readPriceDataFromCSV("GSPC_Prices_Test.csv", true);
		List<Double> benchmarkPrices = benchmarkPriceData.values().stream().collect(Collectors.toList());

		Assertions.assertEquals(255, stockPrices.size());
		Assertions.assertEquals(255, benchmarkPrices.size());
	}
	
	@Test
	@DisplayName("Read dividends data from CSV file")
	public void testReadDividendsDataFromCSV() {
		Map<LocalDate, Double> dividendsData = CSVReader.readDividendsDataFromCSV("AAPL_Dividends_Test.csv", true);
		List<Double> dividends = dividendsData.values().stream().collect(Collectors.toList());
		
		Assertions.assertEquals(4, dividends.size());
	}
	
	@Test
	@DisplayName("Read spreads data from CSV file")
	public void testReadDamodaranSpreadsDataFromCSV() {
		Map<CompanyType, Map<String, List<Double>>> spreads = CSVReader.readDamodaranSpreadsDataFromCSV();
		List<Double> results = new ArrayList<>(Arrays.asList(2.0, 2.2499999, 0.0274));
		Assertions.assertEquals(results, spreads.get(CompanyType.NON_FINANCIAL).get("Ba2/BB"));
	}
	
	@Test
	@DisplayName("Read premiums data from CSV file")
	public void testReadDamodaranPremiumDataFromCSV() {
		Map<Region, Double> premiums = CSVReader.readDamodaranPremiumDataFromCSV();
		Assertions.assertEquals(0.06, premiums.get(Region.NORTH_AMERICA));
	}
}