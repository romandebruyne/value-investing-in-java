package de.personal.value_investing;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.personal.value_investing.utils.BetaUtils;
import de.personal.value_investing.utils.CSVReader;
import de.personal.value_investing.utils.DataInterval;
import de.personal.value_investing.utils.Period;

public class BetaUtilsTest {
	
	@BeforeEach
	public void setup() {
		System.out.println("BetaUtils Test starts!");
	}
	
	@Test
	@DisplayName("Determine required data points")
	public void testDetermineRequiredDataPoints() {
		int requiredDataDaily;
		int requiredDataMonthly;
		
		requiredDataDaily = BetaUtils.determineRequiredDataPoints(DataInterval.ONE_DAY, Period.ONE_YEAR);
		Assertions.assertEquals(253, requiredDataDaily);

		requiredDataMonthly = BetaUtils.determineRequiredDataPoints(DataInterval.ONE_MONTH, Period.ONE_YEAR);
		Assertions.assertEquals(13, requiredDataMonthly);
	}
	
	@Test
	@DisplayName("Create subset")
	public void testCreateSubset() {
		Map<LocalDate, Double> data = CSVReader.readPriceDataFromCSV("AAPL_Prices_Test.csv", true);
		int requiredDataDaily = BetaUtils.determineRequiredDataPoints(DataInterval.ONE_DAY, Period.ONE_YEAR);
		System.out.println(data.size());
		
		List<Double> dataSubset = BetaUtils.createPriceDataSubset(data.values().stream()
				.collect(Collectors.toList()), requiredDataDaily);
		
		Assertions.assertEquals(253, dataSubset.size());
	}
	
	@Test
	@DisplayName("Calculate returns")
	public void testCalculateReturns() {
		Map<LocalDate, Double> data = CSVReader.readPriceDataFromCSV("AAPL_Prices_Test.csv", true);
		int requiredDataDaily = BetaUtils.determineRequiredDataPoints(DataInterval.ONE_DAY, Period.ONE_YEAR);
		
		List<Double> dataSubset = BetaUtils.createPriceDataSubset(data.values().stream()
				.collect(Collectors.toList()),requiredDataDaily);
		
		List<Double> returns = BetaUtils.calculateReturns(dataSubset);
		
		Assertions.assertEquals(253, returns.size());
		Assertions.assertEquals(0.0d, returns.get(0));
	}

}
