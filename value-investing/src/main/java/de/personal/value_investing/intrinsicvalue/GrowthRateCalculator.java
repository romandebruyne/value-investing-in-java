package de.personal.value_investing.intrinsicvalue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.personal.value_investing.evaluation.Evaluator;
import de.personal.value_investing.utils.CalculationUtils;
import de.personal.value_investing.utils.DataInterval;
import de.personal.value_investing.utils.DatabaseUtils;
import de.personal.value_investing.utils.Period;

public class GrowthRateCalculator {
	private GrowthRateCalculator() {}
	
	/*
	 * Calculates Compound Annual Growth Rates (CAGRs) for specified metric and period.
	 * 
	 * @param data		metric data.
	 * @param period	specifies period for which the beta is estimated.
	 * 
	 * @return	List containing the CAGRs.
	 */
	public static List<Double> calculateSingleMetricCAGRs(List<Double> data, Period period) {
		List<Integer> years = new ArrayList<>();
		List<Double> cagrs = new ArrayList<>();
		int steps, periodCounter, yearsDifference;
		double startValue, endValue;
		
		if (period == Period.TEN_YEARS) {
			periodCounter = period.getPeriod() - 1;
		} else {
			periodCounter = period.getPeriod();
		}
			
		for (int i = data.size() - periodCounter; i < data.size(); i++) {
			years.add(i);
		}
		
		steps = 1;
		while (periodCounter > 0) {
			for (int year : years) {
				startValue = data.get(year - 1);
				endValue = data.get(year - 1 + steps);
				yearsDifference = steps;
				cagrs.add(CalculationUtils.roundToXDecimals(calculateCAGR(startValue, endValue,
						yearsDifference), 5));
			}
			
			years.remove(years.size() - 1);
			steps++;
			periodCounter--;
		}
		
		return cagrs;
	}
	
	/*
	 * Calculates one single Compound Annual Growth Rate (CAGR).
	 * 
	 * @param startValue			value at the beginning of period.
	 * @param endValue				value at the end of period.
	 * @param yearsInDifference		specifies the number of years contained in the period.
	 * 
	 * @return	calculated CAGR.
	 */
	public static double calculateCAGR(double startValue, double endValue, int yearsDifference) {
        if (Math.min(startValue, endValue) <= 0) {
        	return Double.NaN;
        } else {
        	return Math.pow(endValue / startValue, (1.0 / yearsDifference)) - 1;
        }
	}
	
	/*
	 * Calculates the median value of a series of Compound Annual Growth Rates (CAGRs).
	 * 
	 * @param compoundAnnualGrowthRates		List containing the CAGRs.
	 * 
	 * @return	calculated median CAGR.
	 */
	public static double calculateMedianCAGR(List<Double> compoundAnnualGrowthRates) {
		if (Evaluator.isDataValid(compoundAnnualGrowthRates)) {
			return CalculationUtils.calculateMedianWithoutNaN(compoundAnnualGrowthRates);
		} else {
			return Double.NaN;
		}
	}
	
	/*
	 * Calculates Compound Annual Growth Rates (CAGRs) for specified benchmark and period.
	 * 
	 * @param benchmarkPriceData	Map containing benchmark price data.
	 * @param interval				specifies interval observed in the price data.
	 * @param period				specifies period for which the CAGRs are estimated.
	 * 
	 * @return	List containing the CAGRs.
	 */
	public static List<Double> calculateBenchmarkCAGRs(Map<LocalDate, Double> benchmarkPriceData,
			DataInterval interval, Period period) {
		List<Double> priceDataSubset = new ArrayList<>();
		int index = benchmarkPriceData.size() - 1;
		
		priceDataSubset.add(getValueAtIndexFromMap(benchmarkPriceData, index));

		for (int i = 1; i <= period.getPeriod(); i++) {
			if (interval == DataInterval.ONE_DAY) {
				index -= 252;
				priceDataSubset.add(getValueAtIndexFromMap(benchmarkPriceData, index));
			} else if (interval == DataInterval.ONE_MONTH) {
				index -= 12;
				priceDataSubset.add(getValueAtIndexFromMap(benchmarkPriceData, index));
			}
		}
		return calculateSingleMetricCAGRs(DatabaseUtils.reverseList(priceDataSubset), period);
	}
	
	/*
	 * Gets data from (sorted) Map at specific index.
	 * 
	 * @param data		Map containing data.
	 * @param index		index to obtain data point.
	 * 
	 * @return	value at specified index.
	 */
	private static double getValueAtIndexFromMap(Map<LocalDate, Double> data, int index) {
		return data.values().stream().collect(Collectors.toList()).get(index);
	}
	
	/*
	 * Prints Compound Annual Growth Rates (CAGRs) and the Return on Equity value.
	 * 
	 * @param cagr				a stock's CAGR.
	 * @param returnOnEquity	Return on Equity value
	 * @param benchmarkCAGR		a benchmark's CAGR.
	 */
	public static void printGrowthRates(double cagr, double returnOnEquity, double benchmarkCAGR) {
		System.out.println("Growth rates under consideration:");
		System.out.printf("%-20s %.1f\n", "CAGR:", cagr);
		System.out.printf("%-20s %.1f\n", "Benchmark CAGR:", benchmarkCAGR);
		System.out.printf("%-20s %.1f\n", "Return on Equity:", returnOnEquity);
	}
	
	/*
	 * Checks whether a given growth rate is NaN.
	 * 
	 * @return true, if growth rate is not NaN.
	 */
	public static boolean growthRateIsValid(double growthRate) {
		return growthRate != Double.NaN;
	}
}