package de.personal.value_investing.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.personal.value_investing.intrinsicvalue.GrowthRateCalculator;

public class IntrinsicValueUtils {
	private IntrinsicValueUtils() {}
	
	/*
	 * Create mapping with discount rates as keys and temporary double values as values.
	 * 
	 * @param discountRates		List object containing discount rate values.
	 * 
	 * @return Map object containing the mapping.
	 */
	public static Map<String, Double> createIntrinsicValueMap(List<Double> discountRates) {
		Map<String, Double> intrinsicValues = new HashMap<>();
		discountRates.forEach(dr -> intrinsicValues.put(String.format("%.1f%%", dr), 0.0d));
		return intrinsicValues;
	}
	
	/*
	 * Adds Intrinsic Value to a Map object.
	 * 
	 * @param intrinsicValueMap		mapping between discount rates and intrinsic values.
	 * @param discountRate			discount rate (serves as new key in Map).
	 * @param valueToAdd			value to add.
	 */
	public static void addToIntrinsicValueMap(Map<String, Double> intrinsicValueMap, double discountRate,
			double valueToAdd) {
		intrinsicValueMap.put(String.format("%.1f%%", discountRate), valueToAdd);
	}
	
	/*
	 * Calculates a Terminal Value, which is required to estimate an Intrinsic value.
	 * 
	 * @param medianCashflow		median cashflow value.
	 * @param growthRate			growth rate assumed for estimation.
	 * @param terminalGrowthRate	terminal growth rate assumed for estimation.
	 * @param discountRate			discount rate assumed for estimation.
	 * @param predictionPeriod		specifies period for which the intrinsic value is estimated.
	 * 
	 * @return Terminal value.
	 */
	public static double calculateTerminalValue(double medianCashflow, double growthRate,
			double terminalGrowthRate, double discountRate, Period predictionPeriod) {
		double firstPartNumerator, firstPartDenominator, secondPart;
		firstPartNumerator = (medianCashflow * Math.pow(1 + growthRate /
				100.0d, predictionPeriod.getPeriod() + 1) * (1 + terminalGrowthRate / 100.0d));
		firstPartDenominator = (discountRate - terminalGrowthRate) / 100.0d;
		secondPart = Math.pow(1 / (1 + discountRate / 100.0d), predictionPeriod.getPeriod() + 1);
		return (firstPartNumerator / firstPartDenominator) * secondPart;
	}
	
	/*
	 * Calculates the Intrinsic Value (estimated via Discounted Cashflow Model) per share.
	 * 
	 * @param discountedCashflows	List object containing discounted cashflows.
	 * @param terminalValue			terminal value.
	 * @param numberOfShares		number of shares.
	 * 
	 * @return the intrinsic value per share.
	 */
	public static double calculateIntrinsticValuePerShare(List<Double> discountedCashflows,
			double terminalValue, double numberOfShares) {
		return CalculationUtils.roundToXDecimals((discountedCashflows.stream().reduce(0.0, (a, b) -> a + b)
				+ terminalValue) / numberOfShares, 2);
	}
	
	/*
	 * Determines the optimal growth rate for Instrinsic Value Estimation. The optimum value is here defined as
	 * the lowest value among three different growth rates.
	 * 
	 * @param metricCAGR		a metric's Compound Annual Growth Rate (CAGR) (as percentage).
	 * @param returnOnEquity	return on equity (as percentage).
	 * @param benchmarkCAGR		a benchmark's CAGR (as percentage).
	 * 
	 * @return the optimal growth rate.
	 */
	public static double determineOptimalGrowthRateForEstimation(double metricCAGR, double returnOnEquity,
			double benchmarkCAGR) {
		double chosenGrowthRate, roundedRoE, roundedBenchmarkCAGR;
		
		if (!GrowthRateCalculator.growthRateIsValid(metricCAGR)) {
			System.err.println("Error: metric CAGR is invalid!");
			chosenGrowthRate = Double.NaN;
		} else {
			chosenGrowthRate = CalculationUtils.roundToXDecimals(metricCAGR, 1);
			roundedRoE = CalculationUtils.roundToXDecimals(returnOnEquity, 1);
			if (benchmarkCAGR != Double.NaN) {
				roundedBenchmarkCAGR = CalculationUtils.roundToXDecimals(benchmarkCAGR, 1);
				GrowthRateCalculator.printGrowthRates(chosenGrowthRate, roundedRoE, roundedBenchmarkCAGR);
				if (chosenGrowthRate > roundedBenchmarkCAGR) {
					chosenGrowthRate = roundedBenchmarkCAGR;
				}
			}
			if (chosenGrowthRate > roundedRoE) {
				chosenGrowthRate = roundedRoE;
			}
			if (chosenGrowthRate < 0) {
				chosenGrowthRate = 0.0d;
			}
		}
		return chosenGrowthRate;
	}
}