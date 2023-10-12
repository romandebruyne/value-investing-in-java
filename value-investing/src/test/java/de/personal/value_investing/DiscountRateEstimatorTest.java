package de.personal.value_investing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.personal.value_investing.database.Database;
import de.personal.value_investing.intrinsicvalue.DiscountRateEstimator;
import de.personal.value_investing.utils.CSVReader;
import de.personal.value_investing.utils.CompanyType;
import de.personal.value_investing.utils.Period;
import de.personal.value_investing.utils.Region;

public class DiscountRateEstimatorTest {
	
	private DiscountRateEstimator estimator;
	private Database data; 
	
	@BeforeEach
	public void setup() {
		System.out.println("DiscountRateEstimator Test starts");
		this.estimator = new DiscountRateEstimator();
		this.data = new Database("AAPL", "XNAS", CSVReader.readMorningstarDataFromCSV("AAPL_MData_Test.csv"));
	}
	
	@Test
	@DisplayName("Estimate discount rate")
	public void testEstimateDiscountRate() {
		this.estimator.estimateDiscountRate(this.data, 0.0d, 1.75d,
				CompanyType.NON_FINANCIAL,
				Region.NORTH_AMERICA, Period.TEN_YEARS);
		Assertions.assertEquals(0.04, this.estimator.getDiscountRate());
	}
}
