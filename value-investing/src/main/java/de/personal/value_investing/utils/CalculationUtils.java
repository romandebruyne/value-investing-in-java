package de.personal.value_investing.utils;

import java.util.List;
import java.util.stream.Collectors;

public class CalculationUtils {
	private CalculationUtils() {}
	
	/*
	 * Rounds double value to specified precision.
	 * 
	 * @param valueToRound		double value to round.
	 * @param digits			specifies the precision.
	 * 
	 * @return rounded value.
	 */
	public static double roundToXDecimals(double valueToRound, int digits) {
		double precision = 1.0d;
		for (int i = 0; i < digits; i++) {
			precision *= 10.0d;
		}
		return (double) Math.round(valueToRound * precision) / precision;
	}
	
	/*
	 * Calculates the mean of a variable.
	 * 
	 * @param data		List object containing the variable's values.
	 * 
	 * @return calculated mean.
	 */
	public static double calculateMean(List<Double> data) {
		double mean = 0.0;
		for (int i = 0; i < data.size(); i++) {
		        mean += data.get(i);
		}
		return mean / data.size();
	}
	
	/*
	 * Calculates the variance of values in a List.
	 * 
	 * @param data		List object containing the variable's values.
	 * 
	 * @return calculated variance.
	 */
	public static double calculateVariance(List<Double> data) {
		double variance = 0.0;
		double mean = calculateMean(data);
		for (int i = 0; i < data.size(); i++) {
		    variance += Math.pow(data.get(i) - mean, 2);
		}
		return variance / data.size();
	}
	
	/*
	 * Calculates the covariance of two variables x and y.
	 * 
	 * @param x		List object containing the values of variable x.
	 * @param y		List object containing the values of variable y.
	 * 
	 * @return calculated covariance.
	 */
	public static double calculateCovariance(List<Double> x, List<Double> y) {
		double meanOfX = calculateMean(x);
		double meanOfY = calculateMean(y);
		double sum = 0.0;
		for (int i = 0; i < x.size(); i++) {
			sum += (x.get(i) - meanOfX) * (y.get(i) - meanOfY);
		}
		return sum / x.size(); 
	}
	
	/*
	 * Calculates the median of a variable without taking NaN values into consideration.
	 * 
	 * @param data		List object containing the variable's values.
	 * 
	 * @return calculated median.
	 */
	public static double calculateMedianWithoutNaN(List<Double> values) {
		List<Double> valuesWithoutNan = values
				.stream()
				.filter(v -> v != Double.NaN)
				.sorted()
				.collect(Collectors.toList());

		if (valuesWithoutNan.size() == 1) {
			return valuesWithoutNan.get(0);
		} else if (valuesWithoutNan.size() % 2 != 0) {
			return valuesWithoutNan.get(valuesWithoutNan.size() / 2);
		} else if (valuesWithoutNan.size() % 2 == 0) {
			return (valuesWithoutNan.get(valuesWithoutNan.size() / 2) +
					valuesWithoutNan.get(valuesWithoutNan.size() / 2 - 1)) / 2;
		} else {
			return Double.NaN;
		}
	}
}