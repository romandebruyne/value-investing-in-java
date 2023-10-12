package de.personal.value_investing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.personal.value_investing.database.MorningstarScraper;

public class MorningstarScraperTest {
	
	@Test
	@DisplayName("Get the Morningstar identifier")
	public void test() {
		Assertions.assertEquals("0P000000GY", MorningstarScraper.getMorningstarIdentifier("XNAS", "AAPL"));
	}
}
