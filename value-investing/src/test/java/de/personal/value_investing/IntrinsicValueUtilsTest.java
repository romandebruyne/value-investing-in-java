package de.personal.value_investing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.personal.value_investing.utils.IntrinsicValueUtils;

public class IntrinsicValueUtilsTest {
	
	@BeforeEach
	public void setup() {
		System.out.println("IntrinsicValueUtils Test starts");
	}
	
	@Test
	@DisplayName("Use DCF Model to estimate Intrinsic Value")
	public void testUseDiscountedCashflowModel() {
		double optimalGrowthRate = IntrinsicValueUtils.determineOptimalGrowthRateForEstimation(
				10.34d, 42.40d, 20.29d);
		
		Assertions.assertEquals(10.3d, optimalGrowthRate);
	}
}
