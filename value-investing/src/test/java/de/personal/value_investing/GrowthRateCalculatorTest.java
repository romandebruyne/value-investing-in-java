package de.personal.value_investing;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.personal.value_investing.database.Database;
import de.personal.value_investing.intrinsicvalue.GrowthRateCalculator;
import de.personal.value_investing.utils.CSVReader;
import de.personal.value_investing.utils.Metric;
import de.personal.value_investing.utils.Period;

public class GrowthRateCalculatorTest {
	
	private Database data = new Database("AAPL", "XNAS", CSVReader.readMorningstarDataFromCSV("AAPL_MData_Test.csv"));
	
	@BeforeEach
	public void setup() {
		System.out.println("GrowthRateCalculator Test starts");
	}
	
	@Test
	@DisplayName("Calculate Median CAGR")
	public void testCalculateMedianCAGR() {
		List<Double> freeCashflowCAGRsOneYear = GrowthRateCalculator.calculateSingleMetricCAGRs(
				this.data.getData().get(Metric.FREE_CASHFLOW), Period.ONE_YEAR);
		List<Double> freeCashflowCAGRsThreeYears = GrowthRateCalculator.calculateSingleMetricCAGRs(
				this.data.getData().get(Metric.FREE_CASHFLOW), Period.THREE_YEARS);
		List<Double> freeCashflowCAGRsTenYears = GrowthRateCalculator.calculateSingleMetricCAGRs(
				this.data.getData().get(Metric.FREE_CASHFLOW), Period.TEN_YEARS);
		
		Assertions.assertEquals(0.1989, GrowthRateCalculator.calculateMedianCAGR(freeCashflowCAGRsOneYear));
		Assertions.assertEquals(0.241285, GrowthRateCalculator.calculateMedianCAGR(freeCashflowCAGRsThreeYears));
		Assertions.assertEquals(0.08843, GrowthRateCalculator.calculateMedianCAGR(freeCashflowCAGRsTenYears));
	}
}
