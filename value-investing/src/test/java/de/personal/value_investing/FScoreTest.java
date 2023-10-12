package de.personal.value_investing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.personal.value_investing.database.Database;
import de.personal.value_investing.evaluation.FScore;
import de.personal.value_investing.utils.CSVReader;

public class FScoreTest {
	
	private FScore fScore;
	private Database data;
	
	@BeforeEach
	public void setup() {
		System.out.println("FScore Test starts");
		this.fScore = new FScore();
		this.data = new Database("AAPL", "XNAS", CSVReader.readMorningstarDataFromCSV("AAPL_MData_Test.csv"));
	}
	
	@Test
	@DisplayName("Calculate F-Score")
	public void testCalculateFScore() {
		this.fScore.calculateFScore(this.data);
		Assertions.assertEquals(6, this.fScore.getFScore());
	}
}
