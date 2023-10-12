package de.personal.value_investing.evaluation;

import static de.personal.value_investing.utils.Metric.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import de.personal.value_investing.intrinsicvalue.GrowthRateCalculator;
import de.personal.value_investing.utils.CalculationUtils;
import de.personal.value_investing.utils.Metric;
import de.personal.value_investing.utils.Period;

public class Evaluator {
	private Evaluator() {}
	
	/*
	 * Builds/creates a mapping in which Compound Annual Growth Rates (CAGRs) of a specified metric are saved.
	 * 
	 * @param name		metric name.
	 * @param database	database containing the metric's data.
	 * 
	 * @return	Map object containing the CAGRs.
	 */
	public static Map<String, Double> generateCAGRMapping(Metric name, Map<Metric, List<Double>> database) {
		List<Double> originalValues;
		List<Double> cagrs;
		Map<String, Double> cagrMapping = new HashMap<>();
		
		if (name == CAP_EX) {
			originalValues = database.get(CAP_EX)
					.stream()
					.map(v -> v * (-1))
					.collect(Collectors.toList());
		} else {
			originalValues = database.get(name);
		}
		
		for (Period period : Period.values()) {
			cagrs = GrowthRateCalculator.calculateSingleMetricCAGRs(originalValues, period);
			
			if (isDataValid(cagrs)) {
				cagrMapping.put(period.getPeriod() + "Y", CalculationUtils.roundToXDecimals(
						CalculationUtils.calculateMedianWithoutNaN(cagrs), 5));
			} else {
				cagrMapping.put(period.getPeriod() + "Y", Double.NaN);
			}
			
			cagrs = new ArrayList<>();
		}
		return cagrMapping;
	}
	
	/*
	 * Builds/creates a mapping in which median values of a specified metric are saved.
	 * 
	 * @param name		metric name.
	 * @param database	database containing the metric's data.
	 * 
	 * @return	Map object containing the median values.
	 */
	public static Map<String, Double> generateMedianMapping(Metric name, Map<Metric, List<Double>> database) {
		List<Double> medians = new ArrayList<>();
		Map<String, Double> medianMapping = new HashMap<>();
		
		for (Period period : Period.values()) {
			for (int i = database.get(name).size() - period.getPeriod(); i < database.get(name).size(); i++) {
				medians.add(database.get(name).get(i));
			}
			
			if (isDataValid(medians)) {
				medianMapping.put(period.getPeriod() + "Y",
						CalculationUtils.roundToXDecimals(
								CalculationUtils.calculateMedianWithoutNaN(medians), 5));
			} else {
				medianMapping.put(period.getPeriod() + "Y", Double.NaN);
			}
			medians = new ArrayList<>();
		}
		return medianMapping;
	}
	
	/*
	 * Checks whether a list contains valid data, i.e. more than 50 percent of the data points are not NaN.
	 * 
	 * @param data	List object containing data to check.
	 * 
	 * @return true, if data is valid.
	 */
	public static boolean isDataValid(List<Double> data) {
		long notANumberCount = data.stream().filter(n -> n == Double.NaN).count();
		return notANumberCount > data.size() * 0.5 ? false : true;
	}
	
	/*
	 * Assesses an estimated Intrinsic Value by comparing it with the actual stock's price. If desired, a
	 * Margin Of Safety (as percentage) can be specified to account for estimation uncertainty.
	 * 
	 * @param intrinsicValue	value to assess.
	 * @param marginOfSafety	Margin of Safety as percentage.
	 */
	public static void assessIntrinsicValues(Map<String, Double> intrinsicValues, double marginOfSafety,
			double currentStockPrice) {
		double intrinsicValueMinusMoS;
		String assessment = null;
		
		for (Entry<String, Double> entry : intrinsicValues.entrySet()) {
			intrinsicValueMinusMoS = entry.getValue() * (1 - marginOfSafety / 100.0d);
			
			System.out.printf("%-28s %s\n", "Discount Rate:", entry.getKey());
			System.out.printf("%-28s %.2f\n", "Intrinsic Value (IV):", entry.getValue());
			System.out.printf("%-28s %.2f\n", "Margin of Safety (MoS):", marginOfSafety);
			System.out.printf("%-28s %.2f\n", "IV minus MoS:", intrinsicValueMinusMoS);
			System.out.printf("%-28s %.2f\n", "Current Stock Price:", currentStockPrice);
			
			if (intrinsicValueMinusMoS > currentStockPrice) {
				assessment = "Stock is currently undervalued!";
			} else if (currentStockPrice > intrinsicValueMinusMoS) {
				assessment = "Stock is currently overvalued!";
			} else {
				assessment = "Indifference." + "\n";
			}
			System.out.printf("%-28s %s\n\n", "Assessment:", assessment);
		}
	}
}