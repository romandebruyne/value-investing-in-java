package de.personal.value_investing.utils;

import static de.personal.value_investing.utils.Metric.DEBT_RATIO;
import static de.personal.value_investing.utils.Metric.EQUITY_RATIO;
import static de.personal.value_investing.utils.Metric.INTEREST_COVERAGE_RATIO;
import static de.personal.value_investing.utils.Metric.TAX_RATE;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.personal.value_investing.database.Database;
import de.personal.value_investing.evaluation.Evaluator;

public class DiscountRateUtils {
	private DiscountRateUtils() {}
	
	/*
	 * Calculates a metric's median value.
	 * 
	 * @param data		List containing the metric's data.
	 * @param period	specifies the period for which the median is calculated.
	 * 
	 * @return median value.
	 */
	public static double calculateMetricMedianValue(List<Double> data, Period period) {
		List<Double> interestCoverageRatioDataSubset = DatabaseUtils.createMorningstarDataSubset(data, period);
		
		if (Evaluator.isDataValid(interestCoverageRatioDataSubset)) {
			return CalculationUtils.calculateMedianWithoutNaN(interestCoverageRatioDataSubset);
		} else {
			return Double.NaN;
		}
	}
	
	/*
	 * Estimates the Debt Cost of a stock/company.
	 * 
	 * @param data				stock data.
	 * @param riskFreeRate		risk free rate.
	 * @param companyType		specifies the company's type.
	 * @param period			specifies the period for which the debt cost are calculated.
	 * 
	 * @return estimated debt cost.
	 */
	public static double estimateDebtCost(Database data, double riskFreeRate,
			CompanyType companyType, Period period) {
		Map<String, List<Double>> spreads = CSVReader.readDamodaranSpreadsDataFromCSV().get(companyType);
		double medianTaxRate, medianInterestCoverageRatio;
		double spread = Double.NaN, debtCostBeforeTax = Double.NaN, debtCostAfterTax = Double.NaN;
		
		if (!spreads.isEmpty()) {
			medianTaxRate = calculateMetricMedianValue(data.getData().get(INTEREST_COVERAGE_RATIO), period);
			medianInterestCoverageRatio = calculateMetricMedianValue(data.getData().get(TAX_RATE), period);
			spread = determineSpread(spreads, medianInterestCoverageRatio);
			debtCostBeforeTax = riskFreeRate + spread;
			debtCostAfterTax = debtCostBeforeTax * (1 - (medianTaxRate / 100.0d));
		} else {
			System.err.println("Error: cannot estimate debt cost.");
		}
		return debtCostAfterTax;
	}
	
	/*
	 * Estimates the Equity Cost of a stock/company.
	 * 
	 * @param riskFreeRate		risk free rate.
	 * @param beta				beta factor of the stock.
	 * @param region			specifies the region in which the company is located.
	 * 
	 * @return estimated equity cost.
	 */
	public static double estimateEquityCost(double riskFreeRate, double beta, Region region) {
		Map<Region, Double> premiums = CSVReader.readDamodaranPremiumDataFromCSV();
		double premium = Double.NaN;
		
		if (!premiums.isEmpty()) {
			premium = premiums.get(region);
		} else {
			System.err.println("Error: cannot estimate equity cost.");
		}
		
		return riskFreeRate + (premium * beta);
	}
	
	/*
	 * Estimates a stock's/company's Capital Structure (Equity Ratio and Debt Ratio).
	 * 
	 * @param data		stock data.
	 * @param period	specifies the period for which the Capital Structure is estimated.
	 * 
	 * @return Map object containing the Capital Structure.
	 */
	public static Map<Metric, Double> estimateCapitalStructure(List<Double> data, Period period) {
		Map<Metric, Double> capitalStructure = new HashMap<>();
		List<Double> equityRatioDataSubset = DatabaseUtils.createMorningstarDataSubset(data, period);
		
		if (Evaluator.isDataValid(equityRatioDataSubset)) {
			capitalStructure.put(EQUITY_RATIO, CalculationUtils.roundToXDecimals(
					CalculationUtils.calculateMedianWithoutNaN(equityRatioDataSubset) / 100.0d, 2));
			capitalStructure.put(DEBT_RATIO, 1 - capitalStructure.get(EQUITY_RATIO));
		} else {
			System.err.println("Error: cannot estimate capital structure.");
			capitalStructure.put(EQUITY_RATIO, Double.NaN);
			capitalStructure.put(DEBT_RATIO, Double.NaN);
		}
		return capitalStructure;
	}
	
	/*
	 * Determines the spread by comparing the stock's/company's interest coverage ratio with the values
	 * provided by Prof. Damodaran.
	 * 
	 * @param spreads					Map object containing spreads obtained from Pfof. Damodaran's webpage. 
	 * @param interestCoverageRatio		stock's/company's interest coverage ratio.
	 * 
	 * @return spread value.
	 */
	public static double determineSpread(Map<String, List<Double>> spreads, double interestCoverageRatio) {
		double spread = Double.NaN;
		for (Entry<String, List<Double>> entry : spreads.entrySet()) {
			if (interestCoverageRatio > entry.getValue().get(0) &&
					interestCoverageRatio <= entry.getValue().get(1)) {
				return spread = entry.getValue().get(2);
			}
		}
		return spread;
	}
	
	/*
	 * Checks double values for NaN value.
	 * 
	 * @param values	values to check.
	 * 
	 * @return true, if at least one value is NaN.
	 */
	public static boolean isNaNValue(double... values) {
		for (int i = 0; i < values.length; i++) {
			if (values[i] == Double.NaN) {
				return true;
			}
		}
		return false;
	}
}