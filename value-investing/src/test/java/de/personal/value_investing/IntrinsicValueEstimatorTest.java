package de.personal.value_investing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.personal.value_investing.database.Database;
import de.personal.value_investing.intrinsicvalue.IntrinsicValueEstimator;
import de.personal.value_investing.utils.CSVReader;
import de.personal.value_investing.utils.Metric;
import de.personal.value_investing.utils.Period;

public class IntrinsicValueEstimatorTest {
	
	private IntrinsicValueEstimator estimator;
	private Database data = new Database("AAPL", "XNAS", CSVReader.readMorningstarDataFromCSV("AAPL_MData_Test.csv"));
	
	@BeforeEach
	public void setup() {
		System.out.println("IntrinsicValueEstimator Test starts");
		this.estimator = new IntrinsicValueEstimator();
	}
	
	@Test
	@DisplayName("Use DCF Model to estimate Intrinsic Value")
	public void testUseDiscountedCashflowModel() {
		double numberOfShares = this.data.getData().get(Metric.OUTSTANDING_SHARES).get(9);
		this.estimator.addDiscountRates(6.0d, 7.0d, 8.0d, 9.0d, 10.0d);
		this.estimator.useDiscountedCashflowModel(this.data,
				Metric.FREE_CASHFLOW,
				numberOfShares, 8.0d, 2.0d,
				Period.TEN_YEARS);
		
		Assertions.assertEquals(76.77, this.estimator.getIntrinsicValues().get("10.0%"));
	}
}
