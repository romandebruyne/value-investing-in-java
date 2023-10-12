package de.personal.value_investing.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.personal.value_investing.database.Database;

public class FScoreUtils {
	private FScoreUtils() {}
	
	/*
	 * Creates mapping between metrics (keys) and temporary integer numbers (values).
	 * 
	 * @return mapping as Map object.
	 */
	public static Map<String, Integer> generateFScoreMapping() {
		Map<String, Integer> fScoreMapping = new HashMap<>();
		fScoreMapping.put("Net Income > 0", 0);
		fScoreMapping.put("Operating Cashflow > 0", 0);
		fScoreMapping.put("Op. Cashflow > Net Income", 0);
		fScoreMapping.put("Higher RoA", 0);
		fScoreMapping.put("Lower Debt-to-Equity-Ratio", 0);
		fScoreMapping.put("Higher Current Ratio", 0);
		fScoreMapping.put("Lower/stable Shares", 0);
		fScoreMapping.put("Higher Gross Margin", 0);
		fScoreMapping.put("Higher Asset Turnover", 0);
		return fScoreMapping;
	}
	
	/*
	 * Evaluates a Piotroski F-Score criterion by comparing values.
	 * 
	 * @param firstValue	value one.
	 * @param secondValue	value two.
	 * @param operator		specifies the operator for the comparison.
	 * 
	 * @return one point, if evaluation is positive.
	 */
	public static int evaluateCriterion(double firstValue, double secondValue, String operator) {
		int point = 0;
		
		if (firstValue != Double.NaN && secondValue != Double.NaN) {
			switch (operator) {
			case "<":
				if (firstValue < secondValue) {
					point++;
				}
				break;
			case ">":
				if (firstValue > secondValue) {
					point++;
				}
				break;
			case "<=":
				if (firstValue <= secondValue) {
					point++;
				}
				break;
			case ">=":
				if (firstValue >= secondValue) {
					point++;
				}
				break;
			}
		}
		return point;
	}
	
	/*
	 * Gets data point at specified position in List object.
	 * 
	 * @param data			stock data.
	 * @param metric		specifies the desired metric.
	 * @param position		specifies index position of desired data point in List.
	 * 
	 * @return desired data point.
	 */
	public static double getDatapoint(Database data, Metric metric, int position) {
		List<Double> datapoints = new ArrayList<>();
	
		try {
			datapoints = data.getData().get(metric);
		} catch (IllegalArgumentException iae) {
			System.err.println("Metric " + metric + " not found!");
		}
		
		if (!datapoints.isEmpty()) {
			return datapoints.get(datapoints.size() - position);			
		} else {
			return Double.NaN;
		}
	}
}