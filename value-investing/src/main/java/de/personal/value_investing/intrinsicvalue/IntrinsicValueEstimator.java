package de.personal.value_investing.intrinsicvalue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.personal.value_investing.database.Database;
import de.personal.value_investing.evaluation.Evaluator;
import de.personal.value_investing.utils.CalculationUtils;
import de.personal.value_investing.utils.IntrinsicValueUtils;
import de.personal.value_investing.utils.Metric;
import de.personal.value_investing.utils.Period;

public class IntrinsicValueEstimator {
	private List<Double> discountRates;
	private Map<String, Double> intrinsicValues;
	
	public IntrinsicValueEstimator() {
		this.discountRates = new ArrayList<>();
	}
	
	public Map<String, Double> getIntrinsicValues() {
		return this.intrinsicValues;
	}
	
	/*
	 * Estimates an Intrinsic Value using the Discounted Cashflow Model (DCF).
	 * 
	 * @param database				stock data.
	 * @param metric				metric on which the estimation is based.
	 * @param numberOfShares		number of (outstanding) shares)
	 * @param growthRate			growth rate assumed for estimation (as percentage).
	 * @param terminalGrowthRate	terminal growth rate assumed for estimation (as percentage).
	 * @param predictionPeriod		specifies period for which the intrinsic value is estimated.
	 */
	public void useDiscountedCashflowModel(Database database, Metric metric, double numberOfShares,
			double growthRate, double terminalGrowthRate, Period predictionPeriod) {
		List<Integer> years = new ArrayList<>();
		List<Double> cashflows = new ArrayList<>();
		List<Double> medianCashflows = new ArrayList<>();
		List<Double> discountedCashflows = new ArrayList<>();
		double terminalValue, intrinsicValuePerShare = Double.NaN;
		
		if (!Evaluator.isDataValid(database.getData().get(metric)) || this.discountRates.isEmpty()) {
			System.err.println("Error: cannot use DCF Model to estimate Intrinsic Value.");
		} else {
			for (int i = 0; i <= predictionPeriod.getPeriod(); i++) {
				years.add(i);
			}
			cashflows = database.getData().get(metric);
			this.intrinsicValues = IntrinsicValueUtils.createIntrinsicValueMap(this.discountRates);
			
			for (double dr : this.discountRates) {
				medianCashflows.add(CalculationUtils.calculateMedianWithoutNaN(cashflows));
				
				for (int year : years) {
					medianCashflows.add(medianCashflows.get(medianCashflows.size() - 1) *
							(1 + growthRate / 100.0d));
					discountedCashflows.add(medianCashflows.get(medianCashflows.size() - 1) /
							Math.pow(1 + dr / 100.0d, year));
				}
					
				terminalValue = IntrinsicValueUtils.calculateTerminalValue(medianCashflows.get(0),
						growthRate, terminalGrowthRate, dr, predictionPeriod);
				
				intrinsicValuePerShare = IntrinsicValueUtils.calculateIntrinsticValuePerShare(
						discountedCashflows, terminalValue, numberOfShares);
				IntrinsicValueUtils.addToIntrinsicValueMap(this.intrinsicValues, dr,
						intrinsicValuePerShare);
				
				medianCashflows.clear();
				discountedCashflows.clear();
			}
		}
	}
	
	/*
	 * Adds values to discount rate list.
	 * 
	 * @param discountRates		variable amount of double values.
	 */
	public void addDiscountRates(double... discountRates) {
		for (double rate : discountRates) {
			this.discountRates.add(rate);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Intrinsic Values using DCF Model\n");
		for (Entry<String, Double> entry : this.intrinsicValues.entrySet()) {
			sb.append(String.format("%-10s %.2f\n", entry.getKey() + ":", entry.getValue()));
		}
		return sb.toString();
	}
}
