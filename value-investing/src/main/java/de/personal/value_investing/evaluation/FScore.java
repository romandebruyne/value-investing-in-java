package de.personal.value_investing.evaluation;

import static de.personal.value_investing.utils.Metric.*;

import java.util.Map;
import java.util.Map.Entry;

import de.personal.value_investing.database.Database;
import de.personal.value_investing.utils.FScoreUtils;

public class FScore {
	private Map<String, Integer> fScoreMapping;
	private int fScore;

	public FScore() {
		this.fScoreMapping = FScoreUtils.generateFScoreMapping();
	}
	
	public int getFScore() {
		return this.fScore;
	}

	/*
	 * Calculates a stock's Piotroski F-Score.
	 * 
	 * @param database	stock data.
	 */
	public void calculateFScore(Database database) {
		double netIncomeLatest = FScoreUtils.getDatapoint(database, NET_INCOME, 1);
		double operatingCashflowLatest = FScoreUtils.getDatapoint(database, OPERATING_CASHFLOW, 1);
		double returnOnAssetsLatest = FScoreUtils.getDatapoint(database, ROA, 1);
		double returnOnAssetsPrevious = FScoreUtils.getDatapoint(database, ROA, 2);
		double debtToEquityLatest = FScoreUtils.getDatapoint(database, DEBT_TO_EQUITY_RATIO, 1);
		double debtToEquityPrevious = FScoreUtils.getDatapoint(database, DEBT_TO_EQUITY_RATIO, 2);
		double currentRatioLatest = FScoreUtils.getDatapoint(database, CURRENT_RATIO, 1);
		double currentRatioPrevious = FScoreUtils.getDatapoint(database, CURRENT_RATIO, 2);
		double sharesLatest = FScoreUtils.getDatapoint(database, OUTSTANDING_SHARES, 1);
		double sharesPrevious = FScoreUtils.getDatapoint(database, OUTSTANDING_SHARES, 2);
		double grossMarginLatest = FScoreUtils.getDatapoint(database, GROSS_MARGIN, 1);
		double grossMarginPrevious = FScoreUtils.getDatapoint(database, GROSS_MARGIN, 2);
		double assetTurnoverLatest = FScoreUtils.getDatapoint(database, ASSET_TURNOVER, 1);
		double assetTurnoverPrevious = FScoreUtils.getDatapoint(database, ASSET_TURNOVER, 2);

		this.fScoreMapping.put("Net Income > 0", FScoreUtils.evaluateCriterion(netIncomeLatest, 0, ">"));
		this.fScoreMapping.put("Operating Cashflow > 0",
				FScoreUtils.evaluateCriterion(operatingCashflowLatest, 0, ">"));
		this.fScoreMapping.put("Op. Cashflow > Net Income",
				FScoreUtils.evaluateCriterion(operatingCashflowLatest, netIncomeLatest, ">"));
		this.fScoreMapping.put("Higher RoA",
				FScoreUtils.evaluateCriterion(returnOnAssetsLatest, returnOnAssetsPrevious, ">"));
		this.fScoreMapping.put("Lower Debt-to-Equity-Ratio",
				FScoreUtils.evaluateCriterion(debtToEquityLatest, debtToEquityPrevious, "<"));
		this.fScoreMapping.put("Higher Current Ratio",
				FScoreUtils.evaluateCriterion(currentRatioLatest, currentRatioPrevious, ">"));
		this.fScoreMapping.put("Lower/stable Shares",
				FScoreUtils.evaluateCriterion(sharesLatest, sharesPrevious, "<="));
		this.fScoreMapping.put("Higher Gross Margin",
				FScoreUtils.evaluateCriterion(grossMarginLatest, grossMarginPrevious, ">"));
		this.fScoreMapping.put("Higher Asset Turnover",
				FScoreUtils.evaluateCriterion(assetTurnoverLatest, assetTurnoverPrevious, ">"));

		this.fScore = this.fScoreMapping.values().stream().reduce(0, (a, b) -> a + b);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Piotroski F-Score\n");
		for (Entry<String, Integer> entry : this.fScoreMapping.entrySet()) {
			sb.append(String.format("%-30s %d\n", entry.getKey() + ":", entry.getValue()));
		}
		sb.append(String.format("%-30s %d/9\n", "Overall Score:", this.fScore));
		return sb.toString();
	}
}
