package de.personal.value_investing.database;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class DamodaranScraper {
	private static final String PATH = "./res/";
	private static final String SPREADS_URL = "https://pages.stern.nyu.edu/~adamodar/pc/ratings.xls";
	private static final String PREMIUM_URL = "https://pages.stern.nyu.edu/~adamodar/pc/datasets/ctryprem.xlsx";
	public static final String SPREADS_FILE = "ratings.xls";
	public static final String PREMIUMS_FILE = "ctryprem.xlsx";
	
	private DamodaranScraper() {}
	
	/*
	 * Scrapes data on spreads from Prof. Damodaran's webpage.
	 */
	@Deprecated
	public static void getSpreadData() {
		try (InputStream in = new URL(SPREADS_URL).openStream()) {
			Files.copy(in, Paths.get(PATH + SPREADS_FILE), StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			System.err.println("Error during download of spread data from Prof. Damodaran's webpage.");
		}
	}

	/*
	 * Scrapes data on risk premiums from Prof. Damodaran's webpage.
	 */
	@Deprecated
	public static void getRiskPremiumData() {
		try (InputStream in = new URL(PREMIUM_URL).openStream()) {
			Files.copy(in, Paths.get(PATH + PREMIUMS_FILE), StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			System.err.println("Error during download of risk premium data from Prof. Damodaran's webpage.");
		}
	}
}
