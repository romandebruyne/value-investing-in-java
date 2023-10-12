package de.personal.value_investing.utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CSVWriter {
	private static final String PATH = "./res/";
	
	private CSVWriter() {}
	
	/*
	 * Writes stock data obtained from Morningstar to a CSV file.
	 * 
	 * @param mdata			Map containing the Morningstar data.
	 * @param fileName		specifies the CSV file name.
	 */
	public static void writeMorningstarDataToCSV(Map<Metric, List<Double>> mdata, String fileName) {
		try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(PATH + fileName), StandardOpenOption.CREATE)) {
			for (Entry<Metric, List<Double>> entry : mdata.entrySet()) {
				bw.append(makeStringFromMapEntry(entry));
				bw.newLine();
			}
			
		} catch (IOException e) {
			System.out.println("Error during export of Morningstar data to CSV.");
		}
	}
	
	/*
	 * Converts a Map Entry to a String.
	 * 
	 * @param entry		Map Entry to convert.
	 * 
	 * @return String converted from Map Entry.
	 */
	private static String makeStringFromMapEntry(Entry<Metric, List<Double>> entry) {
		String returnString = null;
		
		returnString = entry.getKey().getNameAndNotation() + ";";
		for (double value : entry.getValue()) {
			returnString += String.valueOf(value) + ";";
		}		
			
		return returnString;
	}
}