package de.personal.value_investing.beta;

import java.util.List;

import de.personal.value_investing.utils.BetaUtils;
import de.personal.value_investing.utils.CalculationUtils;
import de.personal.value_investing.utils.DataInterval;
import de.personal.value_investing.utils.Period;

public class BetaEstimator {
	private double beta;
	
	public BetaEstimator() {
		this.beta = Double.NaN;
	}
	
	public double getBeta() {
		return this.beta;
	}
	
	/*
	 * Estimates the beta factor by dividing the covariance of the cumulative returns of stock and benchmark
	 * by the benchmark's variance.
	 * 
	 * @param stockPriceData		List containing stock price data.
	 * @param benchmarkPriceData	List containing benchmark price data.
	 * @param dataInterval			specifies the data interval observed in both Lists.
	 * @param period				specifies period for which the beta is estimated.
	 */
	public void estimateBeta(List<Double> stockPriceData, List<Double> benchmarkPriceData,
			DataInterval dataInterval, Period period) {
		int requiredDataPoints;
		double benchmarkVariance, covariance;
		List<Double> stockReturns;
		List<Double> benchmarkReturns;
		List<Double> cumulativeStockReturns;
		List<Double> cumulativeBenchmarkReturns;
		
		requiredDataPoints = BetaUtils.determineRequiredDataPoints(dataInterval, period);
		if (!BetaUtils.datasetIsLargeEnough(stockPriceData, requiredDataPoints) ||
				!BetaUtils.datasetIsLargeEnough(benchmarkPriceData, requiredDataPoints)) {
			System.err.println("Not enough data points in dataset!");
		} else {
			stockReturns = BetaUtils.calculateReturns(BetaUtils.createPriceDataSubset(stockPriceData,
					requiredDataPoints));
			benchmarkReturns = BetaUtils.calculateReturns(BetaUtils.createPriceDataSubset(benchmarkPriceData,
					requiredDataPoints));

			cumulativeStockReturns = BetaUtils.calculateCumulativeReturns(stockReturns);
			cumulativeBenchmarkReturns = BetaUtils.calculateCumulativeReturns(benchmarkReturns);
			
			benchmarkVariance = CalculationUtils.calculateVariance(cumulativeBenchmarkReturns);
			
			covariance = CalculationUtils.calculateCovariance(cumulativeStockReturns,
					cumulativeBenchmarkReturns);
			
			this.beta = CalculationUtils.roundToXDecimals(covariance / benchmarkVariance, 2);
		}
	}
}