package de.personal.value_investing.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BetaUtils {
	private BetaUtils() {}
	
	/*
	 * Determines the number of data points required for specified estimation periods.
	 * 
	 * @param dataInterval		specifies the interval observed in the data.
	 * @param period			specifies the estimation period.
	 */
	public static int determineRequiredDataPoints(DataInterval dataInterval, Period period) {
		switch (dataInterval) {
		case ONE_DAY:
			return period.getPeriod() * 252 + 1;
		case ONE_MONTH:
			return period.getPeriod() * 12 + 1;
		default:
			System.err.println("Error: invalid interval, please use ONE_DAY or ONE_MONTH.");
			return 0;
		}
	}
	
	/*
	 * Checks whether dataset is large enough (i.e. has enough data points) for estimation.
	 * 
	 * @param data					dataset used for estimation.
	 * @param numberOfDataPoints	number of data points required for estimation.
	 * 
	 * @return true, if dataset is large enough.
	 */
	public static boolean datasetIsLargeEnough(List<Double> data, int numberOfDataPoints) {
		if (data.size() - numberOfDataPoints < 0) {
			return false;
		} else {
			return true;
		}
	}
	
	/*
	 * Creates a Sublist from a List.
	 * 
	 * @param priceData					original List object.
	 * @param numberOfDataPoints		specifies the length of the Sublist.
	 * 
	 * @param the Sublist.		
	 */
	public static List<Double> createPriceDataSubset(List<Double> priceData, int numberOfDataPoints) {
		List<Double> subset = new ArrayList<>();

		if (datasetIsLargeEnough(priceData, numberOfDataPoints)) {
			for (int i = priceData.size() - numberOfDataPoints; i < priceData.size(); i++) {
				subset.add(priceData.get(i));
			}
		}
		return subset;
	}
	
	/*
	 * Calculate returns of Stocks (or Benchmarks) based on price data.
	 * 
	 * @param priceData		List object containing price data.
	 * 
	 * @return List object containing the returns.
	 */
	public static List<Double> calculateReturns(List<Double> priceData) {
		double stockReturn;
		List<Double> stockReturns = new ArrayList<>(Arrays.asList(0.0));
		
		for (int i = 1; i < priceData.size(); i++) {
			stockReturn = priceData.get(i) / priceData.get(i - 1) - 1.0d;
			stockReturns.add(stockReturn);
		}
		return stockReturns;
	}
	
	/*
	 * Calculate cumulative returns of Stocks (or Benchmarks) based on returns.
	 * 
	 * @param returns		List object containing returns.
	 * 
	 * @return List object containing the cumulative returns.
	 */
	public static List<Double> calculateCumulativeReturns(List<Double> returns) {
		double cumulativeStockReturn;
		List<Double> cumulativeStockReturns = new ArrayList<>(Arrays.asList(0.0));
		
		for (int i = 1; i < returns.size(); i++) {
			cumulativeStockReturn = returns.get(i) + 1.0d * cumulativeStockReturns.get(i - 1);
			cumulativeStockReturns.add(cumulativeStockReturn);
		}
		return cumulativeStockReturns;
	}
}