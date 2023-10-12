package de.personal.value_investing.evaluation;

import java.util.List;

import de.personal.value_investing.beta.BetaEstimator;
import de.personal.value_investing.utils.HighReturnsFromLowRiskUtils;

public class HighReturnsFromLowRiskScore {
	private double oneYearBeta;
	private double oneYearMomentum;
	private double oneYearDividendYield;
	private int score;
	
	/*
	 * Calculates the overall 'High Returns from Low Risk' score.
	 * 
	 * @param betaEstimator		object of type BetaEstimator.
	 * @param stockPriceData	List containing stock price data.
	 */
	public void calculateScore(BetaEstimator betaEstimator, List<Double> stockPriceData,
			List<Double> benchmarkPriceData, List<Double> dividendsData) {
		this.oneYearBeta = HighReturnsFromLowRiskUtils.estimateOneYearBeta(betaEstimator, stockPriceData,
				benchmarkPriceData);
		this.oneYearMomentum = HighReturnsFromLowRiskUtils.calculateOneYearMomentum(stockPriceData);
		this.oneYearDividendYield = HighReturnsFromLowRiskUtils.estimateOneYearDividendYield(stockPriceData,
				dividendsData);
		
		if (this.oneYearBeta < 1.0d) {
			this.score++;
		} 
		if (this.oneYearMomentum > 0.0d) {
			this.score++;
		} 
		if (this.oneYearDividendYield >= 0.03d) {
			this.score++;
		} 
	}
	
	public double getOneYearBeta() {
		return this.oneYearBeta;
	}
	
	public double getOneYearMomentum() {
		return this.oneYearMomentum;
	}
	
	public double getOneYearDividendYield() {
		return this.oneYearDividendYield;
	}
	
	public double getScore() {
		return this.score;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("'High Returns from Low Risk' Score\n");
		sb.append(String.format("%-34s %d\n", "One-Year-Beta < 1.0:", this.oneYearBeta < 1.0d ? 1 : 0));
		sb.append(String.format("%-34s %d\n", "One-Year-Momentum > 0.0%:", this.oneYearMomentum > 0.0d ? 1 : 0));
		sb.append(String.format("%-34s %d\n", "One-Year-Dividend Yield > 3.0%:",
				this.oneYearDividendYield >= 0.03d ? 1 : 0));
		sb.append(String.format("%-34s %d/3\n", "Overall Score:", this.score));
		return sb.toString();
	}
}