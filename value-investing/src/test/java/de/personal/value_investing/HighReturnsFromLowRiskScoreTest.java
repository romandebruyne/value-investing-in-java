package de.personal.value_investing;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.personal.value_investing.beta.BetaEstimator;
import de.personal.value_investing.evaluation.HighReturnsFromLowRiskScore;
import de.personal.value_investing.utils.CSVReader;

public class HighReturnsFromLowRiskScoreTest {
	
	private HighReturnsFromLowRiskScore hrflrScore;
	private BetaEstimator betaEstimator;
	
	@BeforeEach
	public void setup() {
		System.out.println("HighReturnsFromLowRisk Test starts");
		this.hrflrScore = new HighReturnsFromLowRiskScore();
		this.betaEstimator = new BetaEstimator();
	}
	
	@Test
	@DisplayName("Calculate 'High Returns from Low Risk' score")
	public void testCalculateScore() {
		Map<LocalDate, Double> stockPriceData = CSVReader.readPriceDataFromCSV("AAPL_Prices_Test.csv", true);
		List<Double> stockPrices = stockPriceData.values().stream().collect(Collectors.toList());

		Map<LocalDate, Double> benchmarkPriceData = CSVReader.readPriceDataFromCSV("GSPC_Prices_Test.csv", true);
		List<Double> benchmarkPrices = benchmarkPriceData.values().stream().collect(Collectors.toList());
		
		Map<LocalDate, Double> dividendsData = CSVReader.readDividendsDataFromCSV("AAPL_Dividends_Test.csv", true);
		List<Double> dividends = dividendsData.values().stream().collect(Collectors.toList());
		
		this.hrflrScore.calculateScore(this.betaEstimator, stockPrices, benchmarkPrices, dividends);
		Assertions.assertEquals(1, this.hrflrScore.getScore());
	}
}