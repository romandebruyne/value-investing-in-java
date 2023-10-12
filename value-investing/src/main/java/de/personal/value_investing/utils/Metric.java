package de.personal.value_investing.utils;

public enum Metric {
	REVENUE("Revenue (MM)"),
	REVENUE_GROWTH("Revenue Growth (%)"),
	OPERATING_INCOME("Operating Income (MM)"),
	OPERATING_INCOME_GROWTH("Operating Income Growth (%)"),
	NET_INCOME("Net Income (MM)"),
	NET_INCOME_GROWTH("Net Income Growth (%)"),
	EPS("EPS"),
	EPS_GROWTH("EPS Growth (%)"),
	DIVIDENDS("Dividends"),
	BVPS("Book Value Per Share"),
	OPERATING_CASHFLOW("Operating Cashflow (MM)"),
	OPERATING_CASHFLOW_GROWTH("Operating Cashflow Growth (%)"),
	FREE_CASHFLOW("Free Cashflow (MM)"),
	FREE_CASHFLOW_GROWTH("Free Cashflow Growth (%)"),
	CAP_EX("Capital Expenditure (MM)"),
	CAP_EX_TO_REVENUE("Capital Expenditure to Revenue (%)"),
	PAYOUT_RATIO("Payout Ratio (%)"),
	INTEREST_COVERAGE_RATIO("Interest Coverage Ratio"),
	TAX_RATE("Tax Rate (%)"),
	OPERATING_MARGIN("Operating Margin (%)"),
	NET_MARGIN("Net Margin (%)"),
	GROSS_MARGIN("Gross Margin (%)"),
	ROE("Return on Equity (%)"),
	ROA("Return on Assets (%)"),
	ROIC("Return on Invested Capital (%)"),
	FREE_CASHFLOW_TO_REVENUE("Free Cashflow to Revenue (%)"),
	FREE_CASHFLOW_TO_SHARES("Free Cashflow to Outstanding Shares (%)"),
	CURRENT_RATIO("Current Ratio"),
	EQUITY_RATIO("Equity Ratio (%)"),
	DEBT_RATIO("Debt Ratio (%)"),
	DEBT_TO_EQUITY_RATIO("Debt-To-Equity Ratio"),
	ASSET_TURNOVER("Asset Turnover"),
	OUTSTANDING_SHARES("Outstanding Shares (MM)"),
	TOTAL_EQUITY("Total Equity (MM)"),
	TOTAL_ASSETS("Total Assets (MM)"),
	UNKNOWN("Unknown");
	
	private String nameAndNotation;
	
	private Metric(String nameAndNotation) {
		this.nameAndNotation = nameAndNotation;
	}
	
	public String getNameAndNotation() {
		return this.nameAndNotation;
	}
	
	public static Metric fromString(String str) {
		Metric metricToReturn = UNKNOWN;
		for (Metric metric : Metric.values()) {
			if (metric.getNameAndNotation().equals(str)) {
				metricToReturn = metric;
			}
		}
		return metricToReturn;
	}
}
