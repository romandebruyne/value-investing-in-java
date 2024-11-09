package de.personal.value_investing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.personal.value_investing.database.MorningstarScraper;

public class MorningstarScraperTest {
	@Test
	@Disabled
	@DisplayName("Get the Morningstar identifier")
	public void testGetMorningstarIdentifier() {
		Assertions.assertEquals("0P000000GY", MorningstarScraper.getMorningstarIdentifier("XNAS", "AAPL"));
		// Assertions.assertEquals("0P000002HD", MorningstarScraper.getMorningstarIdentifier("XNAS", "GOOGL"));
	}
}
