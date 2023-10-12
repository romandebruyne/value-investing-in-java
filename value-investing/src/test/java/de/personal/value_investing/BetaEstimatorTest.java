package de.personal.value_investing;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.personal.value_investing.beta.BetaEstimator;
import de.personal.value_investing.utils.CSVReader;
import de.personal.value_investing.utils.DataInterval;
import de.personal.value_investing.utils.Period;

public class BetaEstimatorTest {
	
	private BetaEstimator estimator;
	
	@BeforeEach
	public void setup() {
		System.out.println("BetaEstimator Test starts!");
		this.estimator = new BetaEstimator();
	}
	
	@Test
	@DisplayName("Estimate beta")
	public void testDetermineRequiredDataPoints() {
		Map<LocalDate, Double> stockPriceData = CSVReader.readPriceDataFromCSV("AAPL_Prices_Test.csv", true);
		List<Double> stockPrices = stockPriceData.values().stream().collect(Collectors.toList());

		Map<LocalDate, Double> benchmarkPriceData = CSVReader.readPriceDataFromCSV("GSPC_Prices_Test.csv", true);
		List<Double> benchmarkPrices = benchmarkPriceData.values().stream().collect(Collectors.toList());
		
		this.estimator.estimateBeta(stockPrices, benchmarkPrices, DataInterval.ONE_DAY, Period.ONE_YEAR);
		Assertions.assertEquals(1.75, estimator.getBeta());
	}
}