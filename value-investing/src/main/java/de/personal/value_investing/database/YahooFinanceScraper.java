package de.personal.value_investing.database;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

import de.personal.value_investing.utils.DataInterval;

public class YahooFinanceScraper {
	private static final String PATH = "./res/";
	private static final String BASE_URL = "https://query1.finance.yahoo.com/v7/finance/download/";

	private YahooFinanceScraper() {}
	
	/*
	 * Scrapes price data for a specified time frame from YahooFinance.
	 * 
	 * @param ticker		ticker symbol of exchange/stock.
	 * @param startDate		start date of time frame as string.
	 * @param endDate		end date of time frame as string.
	 * @param interval		interval of price data.
	 */
	public static void getPriceData(String ticker, String startDate, String endDate, DataInterval interval) {
		String fileName;
		
		if (isValidDate(startDate) && isValidDate(endDate)) {
			String url = BASE_URL + ticker + "?"
					+ "period1=" + generatePeriodCode(startDate) + "&period2=" + generatePeriodCode(endDate)
					+ "&interval=" + interval.getInterval()
					+ "&events=history"
					+ "&includeAdjustedClose=true";
		
			if (ticker.startsWith("^")) {
				fileName = ticker.substring(1) + "_Prices.csv";
			} else {
				fileName = ticker + "_Prices.csv";
			}
			
			try (InputStream in = new URL(url).openStream()) {
				Files.copy(in, Paths.get(PATH + fileName), StandardCopyOption.REPLACE_EXISTING);
			} catch (Exception e) {
				System.err.println("Error: cannot download price data from Yahoo Finance.");
			}
		} else {
			System.err.println("Error: invalid dates submitted.");
		}
	}
	
	/*
	 * Scrapes dividends data for a specified time frame from YahooFinance.
	 * 
	 * @param ticker		ticker symbol of stock.
	 * @param startDate		start date of time frame as string.
	 * @param endDate		end date of time frame as string.
	 * @param interval		interval of dividends data.
	 */
	public static void getDividendsData(String ticker, String startDate, String endDate, 
			DataInterval interval) {
		String fileName;
		
		if (isValidDate(startDate) && isValidDate(endDate)) {
			String url = BASE_URL + ticker + "?"
					+ "period1=" + generatePeriodCode(startDate) + "&period2=" + generatePeriodCode(endDate)
					+ "&interval=" + interval.getInterval()
					+ "&events=div"
					+ "&includeAdjustedClose=true";
		
			if (ticker.startsWith("^")) {
				fileName = ticker.substring(1) + "_Dividends.csv";
			} else {
				fileName = ticker + "_Dividends.csv";
			}
			
			try (InputStream in = new URL(url).openStream()) {
				Files.copy(in, Paths.get(PATH + fileName), StandardCopyOption.REPLACE_EXISTING);
			} catch (Exception e) {
				System.err.println("Error: cannot download dividends data from Yahoo Finance.");
			}
		} else {
			System.err.println("Error: invalid dates submitted.");
		}
	}
	
	/*
	 * Converts date to seconds passed between the date and 1970-01-01. Is required since all dates on
	 * YahooFinance are specified in that format.
	 * 
	 * @param dateAsString		date to convert as string.
	 * 
	 * @return	seconds passed.
	 */
	private static long generatePeriodCode(String dateAsString) {
		LocalDate referenceDate = LocalDate.parse("1970-01-01");
		return Math.abs(ChronoUnit.DAYS.between(referenceDate, LocalDate.parse(dateAsString))) * 86400;
	}
	
	/*
	 * Checks whether string contains a correctly formatted date (YYYY-MM-DD).
	 * 
	 * @param dateAsString		date to check.
	 * 
	 * @return	true, if format is correct.
	 */
    private static boolean isValidDate(String dateAsString) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
        try {
        	LocalDate.parse(dateAsString, dateFormatter);
        } catch (DateTimeParseException dtpe) {
            return false;
        }
        return true;
    }
}
