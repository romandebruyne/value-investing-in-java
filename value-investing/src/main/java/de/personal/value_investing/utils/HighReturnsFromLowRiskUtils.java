package de.personal.value_investing.utils;

import java.util.List;

import de.personal.value_investing.beta.BetaEstimator;

public class HighReturnsFromLowRiskUtils {
	private HighReturnsFromLowRiskUtils() {
	}
	
	/*
	 * Estimates One-Year-Beta. Estimation is based on stock and benchmark price data from Yahoo Finance.
	 * 
	 * @param betaEstimator			Class containing the method for estimation.
	 * @param stockPriceData		List containing stock prices. 
	 * @param benchmarkPriceData	List containing benchmark prices.
	 * 
	 * @return One-Year-Beta.
	 */
	public static double estimateOneYearBeta(BetaEstimator betaEstimator, List<Double> stockPriceData,
			List<Double> benchmarkPriceData) {
		betaEstimator.estimateBeta(stockPriceData, benchmarkPriceData, DataInterval.ONE_DAY, Period.ONE_YEAR);
		return betaEstimator.getBeta();
	}
	
	/*
	 * Calculates the (One-Year-) Momentum. Momemtun is defined as percentual change in price within one trading
	 * year, i.e. approximately 252 (trading) days.
	 * 
	 * @param stockPriceData		List containing stock prices.
	 * 
	 * @return One-Year-Momentum in decimal ((0.05 = 5 Percent Momentum).
	 */
	public static double calculateOneYearMomentum(List<Double> stockPriceData) {
		double currentPrice, priceOneYearAgo, oneYearMomentum = Double.NaN;
		try {
			currentPrice = stockPriceData.get(stockPriceData.size() - 1);
			priceOneYearAgo = stockPriceData.get(stockPriceData.size() - 1 - 252);
			oneYearMomentum = CalculationUtils.roundToXDecimals((currentPrice / priceOneYearAgo) - 1, 2);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("Error: cannot calculate One-Year-Momentum.");
		}
		return oneYearMomentum;
	}
	
	/*
	 * Estimates One-Year-Dividend Yield. Estimation is based on price and dividends data from Yahoo Finance.
	 * 
	 * @param stockPriceData	List containing stock prices.
	 * @param dividendsData		List containing dividends.
	 * 
	 * @return One-Year-Dividend Yield in decimal (0.05 = 5 Percent Dividend Yield)
	 */
	public static double estimateOneYearDividendYield(List<Double> stockPriceData, List<Double> dividendsData) {
		double dividendsSum = 0.0d, currentPrice, oneYearDividendYield = Double.NaN;
		if (dividendsData.size() < 4) {
			System.err.println("Error: cannot estimate One-Year-Dividend Yield.");
			return Double.NaN;
		} else {
			for (int i = 0; i < 4; i++) {
				dividendsSum += dividendsData.get(dividendsData.size() - 1 - i);
			}
			currentPrice = stockPriceData.get(stockPriceData.size() - 1);
			oneYearDividendYield = CalculationUtils.roundToXDecimals(dividendsSum / currentPrice, 5);
		}
		return oneYearDividendYield;
	}
}