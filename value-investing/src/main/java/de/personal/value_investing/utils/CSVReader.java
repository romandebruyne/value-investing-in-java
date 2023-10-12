package de.personal.value_investing.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import de.personal.value_investing.database.DamodaranScraper;

public class CSVReader {
	private static final String PATH = "./res/";
	
	private CSVReader() {}
	
	/*
	 * Reads price data (obtained from YahooFinance) from CSV file.
	 * 
	 * @param fileName		CSV file's name.
	 * @param header		specifies whether file contains header.
	 * 
	 * @return	Map object containing price data.
	 */
	public static Map<LocalDate, Double> readPriceDataFromCSV(String fileName, boolean header) {
		String line = null;
		String[] lineData = new String[7];
		LocalDate tempDate;
		Map<LocalDate, Double> priceData = new TreeMap<>();
		
		try (BufferedReader br = Files.newBufferedReader(Paths.get(PATH + fileName))) {
			if (header) {
				line = br.readLine();
			}
			
			while ((line = br.readLine()) != null) {
				lineData = line.split(",");
				tempDate = LocalDate.parse(lineData[0]);
				
				if (lineData[5].contains(",")) {
					lineData[5] = lineData[5].split(",")[0] + "." + lineData[5].split(",")[1];
				}
				
				priceData.put(tempDate, Double.parseDouble(lineData[1]));
			}
			
		} catch (IOException e) {
			System.err.println("Error: cannot import price data from CSV.");
		}
		
		return priceData;
	}
	
	/*
	 * Reads dividends data (obtained from YahooFinance) from CSV file.
	 * 
	 * @param fileName		CSV file's name.
	 * @param header		specifies whether file contains header.
	 * 
	 * @return	Map object containing dividends data.
	 */
	public static Map<LocalDate, Double> readDividendsDataFromCSV(String fileName, boolean header) {
		String line = null;
		String[] lineData = new String[2];
		LocalDate tempDate;
		Map<LocalDate, Double> priceData = new TreeMap<>();
		
		try (BufferedReader br = Files.newBufferedReader(Paths.get(PATH + fileName))) {
			if (header) {
				line = br.readLine();
			}
			
			while ((line = br.readLine()) != null) {
				lineData = line.split(",");
				tempDate = LocalDate.parse(lineData[0]);
				
				if (lineData[1].contains(",")) {
					lineData[1] = lineData[1].split(",")[1] + "." + lineData[1].split(",")[1];
				}
				
				priceData.put(tempDate, Double.parseDouble(lineData[1]));
			}
			
		} catch (IOException e) {
			System.err.println("Error: cannot import dividends data from CSV.");
		}
		
		return priceData;
	}
	
	/*
	 * Reads stock data (obtained from Morningstar.com) from CSV file.
	 * 
	 * @param fileName		CSV file's name.
	 * 
	 * @return	Map object containing stock data.
	 */
	public static Map<Metric, List<Double>> readMorningstarDataFromCSV(String fileName) {
		String line = null;
		String[] lineData = new String[11]; 
		Map<Metric, List<Double>> mdata = new HashMap<>();
		Metric name;
		
		try (BufferedReader br = Files.newBufferedReader(Paths.get(PATH + fileName))) {
			while ((line = br.readLine()) != null) {
				lineData = line.split(";");
				
				if (lineData[0].trim().charAt(1) == 'R' && lineData[0].trim().charAt(2) == 'O'
						&& lineData[0].trim().charAt(3) == 'E') {
					name = Metric.ROE;
				} else {
					name = Metric.fromString(lineData[0].trim());
				}
				
				mdata.put(name, new ArrayList<>());
				for (int i = 1; i < lineData.length; i++) {
					if (lineData[i].contains(",")) {
						lineData[i] = lineData[i].split(",")[0] + "." + lineData[i].split(",")[1];
					}
					mdata.get(name).add(Double.parseDouble(lineData[i]));
				}
			}
			
		} catch (IOException e) {
			System.err.println("Error: cannot import Morningstar data from CSV.");
		}
		
		return mdata;
	}
	
	/*
	 * Reads data on spreads (obtained from Prof. Damodaran's webpage) from CSV file.
	 */
	public static Map<CompanyType, Map<String, List<Double>>> readDamodaranSpreadsDataFromCSV() {
		Map<CompanyType, Map<String, List<Double>>> spreads = new HashMap<>();
		List<Double> values = new ArrayList<>();
		int columnIndex = -1;
		boolean continueReadData = true;
		String key = null;
		
		spreads.put(CompanyType.FINANCIAL, new HashMap<>());
		spreads.put(CompanyType.NON_FINANCIAL, new HashMap<>());
		
		try (FileInputStream file = new FileInputStream(new File(PATH + DamodaranScraper.SPREADS_FILE));
				HSSFWorkbook workbook = new HSSFWorkbook(file);) {
			HSSFSheet sheet = workbook.getSheetAt(0);
			
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext() && continueReadData) {
				Row row = rowIterator.next();
				if (row.getRowNum() >= 18 && row.getRowNum() < 33) {
					Iterator<Cell> cellIterator = row.cellIterator();
					
					while (cellIterator.hasNext()) {
						Cell cell = cellIterator.next();
						columnIndex = cell.getColumnIndex();
						if (columnIndex <= 8) {
							switch (columnIndex) {
							case 2:
								key = cell.getStringCellValue();
								spreads.get(CompanyType.NON_FINANCIAL).put(key, values);
								break;
							case 3:
								spreads.get(CompanyType.NON_FINANCIAL).get(key).add(cell.getNumericCellValue());
								values = new ArrayList<>();
								break;
							case 7:
								key = cell.getStringCellValue();
								spreads.get(CompanyType.FINANCIAL).put(key, values);
								break;
							case 8:
								spreads.get(CompanyType.FINANCIAL).get(key).add(cell.getNumericCellValue());
								values = new ArrayList<>();
								break;
							default:
								values.add(cell.getNumericCellValue());
							}
						}
					}
				} else if (row.getRowNum() >= 33) {
					continueReadData = false;
				}
			}
		} catch (IOException e) {
			System.err.println("Error: cannot import Damodaran spreads data from Excel.");
		}
		
		return spreads;
	}
	
	/*
	 * Reads data on risk premiums (obtained from Prof. Damodaran's webpage) from CSV file.
	 */
	public static Map<Region, Double> readDamodaranPremiumDataFromCSV() {
		Map<Region, Double> premiums = new HashMap<>();
		int columnIndex = -1;
		boolean continueReadData = true;
		Region key = null;
		
		try (FileInputStream file = new FileInputStream(new File(PATH + DamodaranScraper.PREMIUMS_FILE));
				XSSFWorkbook workbook = new XSSFWorkbook(file);) {
			XSSFSheet sheet = workbook.getSheet("Regional Simple Averages");
			
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext() && continueReadData) {
				Row row = rowIterator.next();
				if (row.getRowNum() >= 5 && row.getRowNum() < 40) {
					Iterator<Cell> cellIterator = row.cellIterator();
					
					while (cellIterator.hasNext()) {
						Cell cell = cellIterator.next();
						columnIndex = cell.getColumnIndex();
						if (columnIndex <= 3) {
							switch (columnIndex) {
							case 0:
								if (isRelevantCell(cell.getStringCellValue())) {
									key = Region.fromString(cell.getStringCellValue());
									premiums.put(key, 0.0);
								}
								break;
							case 3:
								if (key != null) {
									premiums.put(key, CalculationUtils.roundToXDecimals(
											cell.getNumericCellValue(), 2));
									key = null;	
								}
								break;
							}
						}
					}
				} else if (row.getRowNum() >= 40) {
					continueReadData = false;
				}
			}
		} catch (IOException e) {
			System.err.println("Error: cannot import Damodaran risk premiums data from Excel.");
		}
		
		return premiums;
	}
	
	/*
	 * Checks whether cells in Excel's worksheet contains relevant data.
	 * 
	 * @param cellValue		value in cell.
	 * 
	 * @return true, if cell is relevant.
	 */
	private static boolean isRelevantCell(String cellValue) {
		List<String> relevantCells = new ArrayList<>();
		Arrays.stream(Region.values()).forEach(val -> relevantCells.add(val.getRegion()));
		return relevantCells.stream().anyMatch(val -> cellValue.equals(val));
	}
}
