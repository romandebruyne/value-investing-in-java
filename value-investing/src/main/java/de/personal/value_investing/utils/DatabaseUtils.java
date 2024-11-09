package de.personal.value_investing.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.personal.value_investing.database.MorningstarScraper;

import static de.personal.value_investing.utils.Category.*;
import static de.personal.value_investing.utils.Metric.*;

public class DatabaseUtils {
	private DatabaseUtils() {}
	
	/*
	 * Estimates historical data of specific metric. Estimation uses historical growth rates and the
	 * metric's latest value obtained from Morningstar.com.
	 * 
	 * @param metric		enum constant containing name of metric.
	 * @param latestValue	metric's latest value.
	 * @param growthRates	metric's historical growth rates.
	 * 
	 * @return HashMap containing estimated historical data.
	 */
	public static Map<Metric, List<Double>> estimateHistoricalDataWithGrowthRates(Metric metric,
			Double latestValue, List<Double> growthRates) {
		List<Metric> validMetrics = new ArrayList<>(Arrays.asList(REVENUE, OPERATING_INCOME, NET_INCOME,
				OPERATING_CASHFLOW, FREE_CASHFLOW));
		Map<Metric, List<Double>> historicalData = new HashMap<>();
		List<Double> values = new ArrayList<>();
		double roundedValue;
		
		if (validMetrics.contains(metric)) {
			values.add(latestValue * 1000.0d);			
		} else if (metric == EPS) {
			values.add(latestValue * 1.0d);
		} else {
			return historicalData;
		}
		
		for (int i = growthRates.size() - 1; i > 0; i--) {
			roundedValue = CalculationUtils.roundToXDecimals(values.get(growthRates.size() - 1 - i) / 
					(1.0d + (growthRates.get(i) / 100.0d)), 0);
			values.add(roundedValue);
		}
		
		historicalData.put(metric, reverseList(values));
		return historicalData;
	}
	
	/*
	 * Estimates historical Capital Expenditure data. Estimation uses historical data on Revenue and
	 * Capital Expenditure to Revenue obtained from Morningstar.com.
	 * 
	 * @param revenue				List containing historical Revenue data.
	 * @param capExToRevenueDat		List containing historical Capital Expenditure to Revenue data.
	 * 
	 * @return HashMap containing estimated historical data.
	 */
	public static Map<Metric, List<Double>> estimateHistoricalCapExData(List<Double> revenueData,
			List<Double> capExToRevenueData) {
		Map<Metric, List<Double>> historicalData = new HashMap<>();
		List<Double> values = new ArrayList<>();
		double roundedValue;
		
		for (int i = 0; i < revenueData.size(); i++) {
			roundedValue = CalculationUtils.roundToXDecimals(revenueData.get(i) * capExToRevenueData.get(i) 
					/ -100.0d, 0);
			values.add(roundedValue);
		}
		
		historicalData.put(CAP_EX, values);
		return historicalData;
	}
	
	/*
	 * Estimates historical Outstanding Shares data. Estimation uses historical data on Free Cashflow and
	 * Free Cashflow To Shares obtained from Morningstar.com.
	 * 
	 * @param freeCashflowData				List containing historical Free Cashflow data.
	 * @param freeCashflowToSharesData		List containing historical Free Cashflow To Shares data.
	 * 
	 * @return HashMap containing estimated historical data.
	 */
	public static Map<Metric, List<Double>> estimateHistoricalSharesData(List<Double> freeCashflowData,
			List<Double> freeCashflowToSharesData) {
		Map<Metric, List<Double>> historicalData = new HashMap<>();
		List<Double> values = new ArrayList<>();
		double roundedValue;
		
		for (int i = 0; i < freeCashflowData.size(); i++) {
			if (freeCashflowData.get(i) == Double.NaN || freeCashflowToSharesData.get(i) == Double.NaN) {
				roundedValue = Double.NaN;
			} else {
				roundedValue = CalculationUtils.roundToXDecimals(freeCashflowData.get(i) /
						freeCashflowToSharesData.get(i), 0);
			}
			values.add(roundedValue);
		}
		
		historicalData.put(OUTSTANDING_SHARES, values);
		return historicalData;
	}
	
	/*
	 * Estimates historical Total Equity data. Estimation uses historical data on Book Value Per Share (BVPS)
	 * obtained from Morningstar.com and estimated Outstanding Shares data.
	 * 
	 * @param bvpsData		List containing historical Book Value Per Share data.
	 * @param sharesData	List containing historical Outstandig Shares data.
	 * 
	 * @return HashMap containing estimated historical data.
	 */
	public static Map<Metric, List<Double>> estimateHistoricalTotalEquityData(List<Double> bvpsData,
			List<Double> sharesData) {
		Map<Metric, List<Double>> historicalData = new HashMap<>();
		List<Double> values = new ArrayList<>();
		double roundedValue;
		
		for (int i = 0; i < bvpsData.size(); i++) {
			roundedValue = CalculationUtils.roundToXDecimals(bvpsData.get(i) * sharesData.get(i), 0);
			values.add(roundedValue);
		}
		
		historicalData.put(TOTAL_EQUITY, values);
		return historicalData;
	}
	
	/*
	 * Estimates historical Total Assets data. Estimation uses historical data on Return on Assets (RoA)
	 * obtained from Morningstar.com and estimated Net Income data.
	 * 
	 * @param netIncomeData		List containing historical Net Income data.
	 * @param roaData			List containing historical Return on Assets data.
	 * 
	 * @return HashMap containing estimated historical data.
	 */
	public static Map<Metric, List<Double>> estimateHistoricalTotalAssetsData(List<Double> netIncomeData,
			List<Double> roaData) {
		Map<Metric, List<Double>> historicalData = new HashMap<>();
		List<Double> values = new ArrayList<>();
		double roundedValue;
		
		for (int i = 0; i < netIncomeData.size(); i++) {
			roundedValue = CalculationUtils.roundToXDecimals(netIncomeData.get(i) / (roaData.get(i) / 100.0d), 0);
			values.add(roundedValue);
		}
		
		historicalData.put(TOTAL_ASSETS, values);
		return historicalData;
	}
	
	/*
	 * Estimates historical Equity Ratio data. Estimation uses estimated historical data on
	 * Book Value Per Share (BVPS), Outstandig Shares, Net Income and Return on Assets (RoA).
	 * 
	 * @param bvpsData			List containing historical Book Value Per Share data.
	 * @param sharesData		List containing historical Outstandig Shares data
	 * @param netIncomeData		List containing historical Net Income data.
	 * @param roaData			List containing historical Return on Assets data.
	 * 
	 * @return HashMap containing estimated historical data.
	 */
	public static Map<Metric, List<Double>> estimateHistoricalEquityRatioData(List<Double> bvpsData,
			List<Double> sharesData, List<Double> netIncome, List<Double> roaData) {
		Map<Metric, List<Double>> historicalData = new HashMap<>();
		List<Double> equityData = estimateHistoricalTotalEquityData(bvpsData, sharesData).get(TOTAL_EQUITY);
		List<Double> assetsData = estimateHistoricalTotalAssetsData(netIncome, roaData).get(TOTAL_ASSETS);
		List<Double> values = new ArrayList<>();
		double roundedValue;
		
		for (int i = 0; i < equityData.size(); i++) {
			roundedValue = CalculationUtils.roundToXDecimals(equityData.get(i) / assetsData.get(i) * 100.0d, 0);
			values.add(roundedValue);
		}
		
		historicalData.put(EQUITY_RATIO, values);
		return historicalData;
	}
	
	/*
	 * Reverse the order in a List.
	 * 
	 * @param originalList	List to reverse.
	 * 
	 * @return reversed List.
	 */
	public static List<Double> reverseList(List<Double> originalList) {
		List<Double> reversedList = new ArrayList<>();
		
		for (int i = originalList.size() - 1; i >= 0; i--) {
			reversedList.add(originalList.get(i));
		}
		
		return reversedList;
	}
	
	@Deprecated
	/*
	 * Builds/creates a complete database containing data from Morningstar.com and estimated data.
	 * 
	 * @param exchangeTicker	ticker symbol of exchange.
	 * @param stockTicker		ticker symbol of stock.
	 * 
	 * @return HashMap containing the database.
	 */
	public static Map<Metric, List<Double>> buildCompleteDatabase(String exchangeTicker, String stockTicker) {
		Map<Metric, List<Double>> data = new HashMap<>();
		String morningstarIdentifier = MorningstarScraper.getMorningstarIdentifier(exchangeTicker, stockTicker);
		
		data.putAll(buildGrowthDatabase(morningstarIdentifier));
		
		data.putAll(MorningstarScraper.getEfficiencyData(
				MorningstarScraper.getUrlByCategory(EFFICIENCY, morningstarIdentifier)));
		
		data.putAll(MorningstarScraper.getFinancialHealthData(
				MorningstarScraper.getUrlByCategory(FINANCIAL_HEALTH, morningstarIdentifier)));
		
		data.putAll(buildCashflowDatabase(morningstarIdentifier));
		
		data.putAll(MorningstarScraper.getDividendsData(
				MorningstarScraper.getUrlByCategory(PAYOUTS, morningstarIdentifier)));
		
		data.putAll(estimateHistoricalCapExData(data.get(REVENUE), data.get(CAP_EX_TO_REVENUE)));
		data.remove(CAP_EX_TO_REVENUE);

		data.putAll(estimateHistoricalSharesData(data.get(FREE_CASHFLOW), data.get(FREE_CASHFLOW_TO_SHARES)));
		data.remove(FREE_CASHFLOW_TO_SHARES);
		
		data.putAll(estimateHistoricalEquityRatioData(data.get(BVPS), data.get(OUTSTANDING_SHARES),
				data.get(NET_INCOME), data.get(ROA)));
		
		return data;
	}
	
	public static Map<Metric, List<Double>> buildCompleteDatabase(String morningstarIdentifier) {
		Map<Metric, List<Double>> data = new HashMap<>();
		
		data.putAll(buildGrowthDatabase(morningstarIdentifier));
		
		data.putAll(MorningstarScraper.getEfficiencyData(
				MorningstarScraper.getUrlByCategory(EFFICIENCY, morningstarIdentifier)));
		
		data.putAll(MorningstarScraper.getFinancialHealthData(
				MorningstarScraper.getUrlByCategory(FINANCIAL_HEALTH, morningstarIdentifier)));
		
		data.putAll(buildCashflowDatabase(morningstarIdentifier));
		
		data.putAll(MorningstarScraper.getDividendsData(
				MorningstarScraper.getUrlByCategory(PAYOUTS, morningstarIdentifier)));
		
		data.putAll(estimateHistoricalCapExData(data.get(REVENUE), data.get(CAP_EX_TO_REVENUE)));
		data.remove(CAP_EX_TO_REVENUE);

		data.putAll(estimateHistoricalSharesData(data.get(FREE_CASHFLOW), data.get(FREE_CASHFLOW_TO_SHARES)));
		data.remove(FREE_CASHFLOW_TO_SHARES);
		
		data.putAll(estimateHistoricalEquityRatioData(data.get(BVPS), data.get(OUTSTANDING_SHARES),
				data.get(NET_INCOME), data.get(ROA)));
		
		return data;
	}
	
	/*
	 * Builds/creates a subset of database containing data from Morningstar.com and estimated data.
	 * 
	 * @param morningstarIdentifier		unique identifier used on Morningstar.com to find stock.
	 * 
	 * @return HashMap containing the subset.
	 */
	private static Map<Metric, List<Double>> buildGrowthDatabase(String morningstarIdentifier) {
		Map<Metric, List<Double>> subset = new HashMap<>();
		
		String urlGrowth = MorningstarScraper.getUrlByCategory(GROWTH, morningstarIdentifier);
		String urlFinancials = MorningstarScraper.getUrlByCategory(FINANCIALS, morningstarIdentifier);

		Map<Metric, List<Double>> growthRateData = MorningstarScraper.getGrowthData(urlGrowth);
		Map<Metric, List<Double>> financialsData = MorningstarScraper.getFinancialsData(urlFinancials);
		
		Double latestRevenue = financialsData.get(REVENUE).get(0);
		Double latestOperatingIncome = financialsData.get(OPERATING_INCOME).get(0);
		Double latestNetIncome = financialsData.get(NET_INCOME).get(0);
		Double latestEPS = financialsData.get(EPS).get(0);
		
		subset.putAll(estimateHistoricalDataWithGrowthRates(REVENUE, latestRevenue,
				growthRateData.get(REVENUE_GROWTH)));
		
		subset.putAll(estimateHistoricalDataWithGrowthRates(OPERATING_INCOME, latestOperatingIncome,
				growthRateData.get(OPERATING_INCOME_GROWTH)));

		subset.putAll(estimateHistoricalDataWithGrowthRates(NET_INCOME, latestNetIncome,
				growthRateData.get(NET_INCOME_GROWTH)));

		subset.putAll(estimateHistoricalDataWithGrowthRates(EPS, latestEPS, growthRateData.get(EPS_GROWTH)));

		return subset;
	}
	
	/*
	 * Builds/creates a subset of database containing data from Morningstar.com and estimated data.
	 * 
	 * @param morningstarIdentifier		unique identifier used on Morningstar.com to find stock.
	 * 
	 * @return HashMap containing the subset.
	 */
	private static Map<Metric, List<Double>> buildCashflowDatabase(String morningstarIdentifier) {
		Map<Metric, List<Double>> subset = new HashMap<>();
		
		String urlCashflow = MorningstarScraper.getUrlByCategory(CASHFLOWS, morningstarIdentifier);
		String urlFinancials = MorningstarScraper.getUrlByCategory(FINANCIALS, morningstarIdentifier);

		Map<Metric, List<Double>> cashflowData = MorningstarScraper.getCashflowData(urlCashflow);
		Map<Metric, List<Double>> financialsData = MorningstarScraper.getFinancialsData(urlFinancials);
		
		Double latestOperatingCashflow = financialsData.get(OPERATING_CASHFLOW).get(0);
		Double latestFreeCashflow = financialsData.get(FREE_CASHFLOW).get(0);
		
		subset.putAll(estimateHistoricalDataWithGrowthRates(OPERATING_CASHFLOW, latestOperatingCashflow,
				cashflowData.get(OPERATING_CASHFLOW_GROWTH)));

		subset.putAll(estimateHistoricalDataWithGrowthRates(FREE_CASHFLOW, latestFreeCashflow,
				cashflowData.get(FREE_CASHFLOW_GROWTH)));

		subset.put(FREE_CASHFLOW_TO_REVENUE, cashflowData.get(FREE_CASHFLOW_TO_REVENUE));
		subset.put(FREE_CASHFLOW_TO_SHARES, cashflowData.get(FREE_CASHFLOW_TO_SHARES));
		subset.put(CAP_EX_TO_REVENUE, cashflowData.get(CAP_EX_TO_REVENUE));
		
		return subset;
	}
	
	/*
	 * Creates subset (sublist) from complete data (List).
	 * 
	 * @param data		List containing all data points.
	 * @param period	Determines the length of the subset.
	 * 
	 * @return subset of data.
	 */
	public static List<Double> createMorningstarDataSubset(List<Double> data, Period period) {
		return data.subList(data.size() - period.getPeriod(), data.size());
	}
}
