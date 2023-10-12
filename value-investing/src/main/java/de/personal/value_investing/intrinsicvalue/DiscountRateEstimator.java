package de.personal.value_investing.intrinsicvalue;

import static de.personal.value_investing.utils.Metric.*;

import de.personal.value_investing.database.Database;
import de.personal.value_investing.utils.CalculationUtils;
import de.personal.value_investing.utils.CompanyType;
import de.personal.value_investing.utils.DiscountRateUtils;
import de.personal.value_investing.utils.Period;
import de.personal.value_investing.utils.Region;

public class DiscountRateEstimator {
	private double discountRate = Double.NaN;
	
	public double getDiscountRate() {
		return this.discountRate;
	}
	
	/*
	 * Estimates a discount rate, which can be used to discount cashflows during intrinsic value estimation.
	 * Discount Rate = (Equity Cost * Median Equity Ratio) * (Debt Cost * Median Debt Ratio).
	 * 
	 * @param database			stock data.
	 * @param riskFreeRate		risk free rate as percentage.
	 * @param beta				beta factor.
	 * @param companyType		specifies the company's type.
	 * @param region			specifies region where the company is located.
	 * @param period			specifies period for which the discount rate is estimated.
	 */
	public void estimateDiscountRate(Database database, double riskFreeRate, double beta,
			CompanyType companyType, Region region, Period period) {
		double debtCost = Double.NaN, equityCost = Double.NaN;
		double medianEquityRatio = Double.NaN, medianDebtRatio = Double.NaN;
		
		debtCost = DiscountRateUtils.estimateDebtCost(database, riskFreeRate, companyType, period);
		equityCost = DiscountRateUtils.estimateEquityCost(riskFreeRate, beta, region);
		medianDebtRatio = DiscountRateUtils.estimateCapitalStructure(database.getData()
				.get(EQUITY_RATIO), period).get(DEBT_RATIO);
		medianEquityRatio = DiscountRateUtils.estimateCapitalStructure(database.getData()
				.get(EQUITY_RATIO), period).get(EQUITY_RATIO);
		
		if (!DiscountRateUtils.isNaNValue(debtCost, equityCost, medianDebtRatio, medianEquityRatio)) {
			discountRate = CalculationUtils.roundToXDecimals((equityCost * medianEquityRatio) +
					(debtCost * medianDebtRatio), 2);
		}
	}
}