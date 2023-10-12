package de.personal.value_investing;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.personal.value_investing.database.YahooFinanceScraper;
import de.personal.value_investing.utils.CSVReader;
import de.personal.value_investing.utils.DataInterval;

public class YahooFinanceScraperTest {
	
	@BeforeEach
	public void setup() {
		System.out.println("YahooFinanceScraper Test starts");
	}
	
	@Test
	@DisplayName("Get price data")
	public void testGetPriceData() {
		YahooFinanceScraper.getPriceData("AAPL", "2022-07-17", "2023-07-17", DataInterval.ONE_DAY);		
		YahooFinanceScraper.getPriceData("^GSPC", "2022-07-17", "2023-07-17", DataInterval.ONE_DAY);
		
		Map<LocalDate, Double> stockPriceData = CSVReader.readPriceDataFromCSV("AAPL_Prices.csv", true);
		List<Double> stockPrices = stockPriceData.values().stream().collect(Collectors.toList());

		Map<LocalDate, Double> benchmarkPriceData = CSVReader.readPriceDataFromCSV("GSPC_Prices.csv", true);
		List<Double> benchmarkPrices = benchmarkPriceData.values().stream().collect(Collectors.toList());
		
		Assertions.assertEquals(250, stockPrices.size());
		Assertions.assertEquals(250, benchmarkPrices.size());
	}
	
	@Test
	@DisplayName("Get dividends data")
	public void testGetDividendsData() {
		YahooFinanceScraper.getDividendsData("AAPL", "2022-07-17", "2023-07-17", DataInterval.ONE_DAY);		
		
		Map<LocalDate, Double> dividendsData = CSVReader.readDividendsDataFromCSV("AAPL_Dividends.csv", true);
		List<Double> dividends = dividendsData.values().stream().collect(Collectors.toList());

		Assertions.assertEquals(4, dividends.size());
	}
}