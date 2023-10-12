package de.personal.value_investing.database;

import static de.personal.value_investing.utils.Metric.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import de.personal.value_investing.utils.CalculationUtils;
import de.personal.value_investing.utils.Category;
import de.personal.value_investing.utils.Metric;

public class MorningstarScraper {
	private static final String BASE_URL = "https://www.morningstar.com/stocks/";
	private static final String API_KEY = "lstzFDEOhfFNMLikKa0am9mgEKLBl49T";
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
			+ "(KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36 OPR/99.0.0.0";
	
	private MorningstarScraper() {}
	
	/*
	 * Obtains unique stock identifier used on Morningstar.com.
	 * 
	 * @param exchangeTicker	ticker symbol of exchange.
	 * @param stockTicker		ticker symbol of stock.
	 * 
	 * @return	unique identifier.
	 */
	public static String getMorningstarIdentifier(String exchangeTicker, String stockTicker) {
		Document doc = null;
		String morningstarIdentifier = null;
		String completeUrl = BASE_URL + exchangeTicker + "/" + stockTicker + "/valuation";
		String[] data;
		
		try {
			doc = Jsoup
					.connect(completeUrl)
					.userAgent(USER_AGENT)
					.timeout(60 * 1000)
					.get();
			data = doc.data().split(",");
	    	
			for (int i = 0; i < data.length; i++) {
	    		if (("\"" + stockTicker + "\"").equals(data[i])) {
	    			morningstarIdentifier = data[i - 1].replace("\"", "");
	    		}
			}
		} catch (IOException e) {
			System.err.println("Error: cannot get the Morningstar Identifier.");
		}
		return morningstarIdentifier;
	}
	
	/*
	 * Gets URL based on category.
	 * 
	 * @param category					(metric) category.
	 * @param morningstarIdentifier		unique identifier used on Morningstar.com to find stock.
	 * 
	 * @return URL as string.
	 */
	public static String getUrlByCategory(Category category, String morningstarIdentifier) {
		String url = null;
		
		switch (category) {
		case GROWTH:
			url = "https://api-global.morningstar.com/sal-service/v1/stock/keyStats/growthTable/"
					+ morningstarIdentifier;
			break;
		case EFFICIENCY:
			url = "https://api-global.morningstar.com/sal-service/v1/stock/keyStats/OperatingAndEfficiency/"
					+ morningstarIdentifier;
			break;
		case FINANCIAL_HEALTH:
			url = "https://api-global.morningstar.com/sal-service/v1/stock/keyStats/financialHealth/"
					+ morningstarIdentifier;
			break;
		case CASHFLOWS:
			url = "https://api-global.morningstar.com/sal-service/v1/stock/keyStats/cashFlow/"
					+ morningstarIdentifier;
			break;
		case PAYOUTS:
			url = "https://api-global.morningstar.com/sal-service/v1/stock/dividends/v4/"
					+ morningstarIdentifier + "/data";
			break;
		case FINANCIALS:
			url = "https://api-global.morningstar.com/sal-service/v1/stock/newfinancials/"
					+ morningstarIdentifier + "/annual/summary";
			break;
		}
		return url;
	}
	
	/*
	 * Scrapes 'Growth' data from Morningstar.com.
	 * 
	 * @param url	URL of webpage to scrape data from.
	 * 
	 * @return Map object containing scraped data.
	 */
	public static Map<Metric, List<Double>> getGrowthData(String url) {
		Document doc = null;
		JSONObject jsonBody, jsonData;
		JSONArray jsonDataArray;
		Map<Metric, List<Double>> data = new HashMap<>();
      	data.put(REVENUE_GROWTH, new ArrayList<Double>());
      	data.put(OPERATING_INCOME_GROWTH, new ArrayList<Double>());
      	data.put(NET_INCOME_GROWTH, new ArrayList<Double>());
      	data.put(EPS_GROWTH, new ArrayList<Double>());
		
		try {
			doc = Jsoup
					.connect(url)
					.userAgent(USER_AGENT)
					.header("apikey", API_KEY)
					.ignoreContentType(true)
					.timeout(60 * 1000)
					.get();
			
	    	jsonBody = new JSONObject(doc.body().text());
	    	jsonDataArray = jsonBody.getJSONArray("dataList");
	    	
	    	for (int i = 0; i < 10; i++) {
	    		jsonData = jsonDataArray.getJSONObject(i);
	    		
	    		addToData(data.get(REVENUE_GROWTH), jsonData.getJSONObject("revenuePer").get("yearOverYear"));
	    		addToData(data.get(OPERATING_INCOME_GROWTH),
	    				jsonData.getJSONObject("operatingIncome").get("yearOverYear"));
	    		addToData(data.get(NET_INCOME_GROWTH),
	    				jsonData.getJSONObject("netIncomePer").get("yearOverYear"));
	    		addToData(data.get(EPS_GROWTH), jsonData.getJSONObject("epsPer").get("yearOverYear"));
			}
		} catch (IOException e) {
			System.err.println("Error: cannot scrape Growth Data from Morningstar.com.");
		}
		return data;
	}
	
	/*
	 * Scrapes 'Efficiency' data from Morningstar.com.
	 * 
	 * @param url	URL of webpage to scrape data from.
	 * 
	 * @return Map object containing scraped data.
	 */
	public static Map<Metric, List<Double>> getEfficiencyData(String url) {
		Document doc = null;
		JSONObject jsonBody, jsonData;
		JSONArray jsonDataArray;
		Map<Metric, List<Double>> data = new HashMap<>();
      	data.put(GROSS_MARGIN, new ArrayList<Double>());
      	data.put(NET_MARGIN, new ArrayList<Double>());
      	data.put(OPERATING_MARGIN, new ArrayList<Double>());
      	data.put(TAX_RATE, new ArrayList<Double>());
      	data.put(ROA, new ArrayList<Double>());
      	data.put(ROE, new ArrayList<Double>());
      	data.put(ROIC, new ArrayList<Double>());
      	data.put(INTEREST_COVERAGE_RATIO, new ArrayList<Double>());
      	data.put(ASSET_TURNOVER, new ArrayList<Double>());
		
		try {
			doc = Jsoup
					.connect(url)
					.userAgent(USER_AGENT)
					.header("apikey", API_KEY)
					.ignoreContentType(true)
					.timeout(60* 1000)
					.get();
			
	    	jsonBody = new JSONObject(doc.body().text());
	    	jsonDataArray = jsonBody.getJSONArray("dataList");
	    	
	    	for (int i = 0; i < 10; i++) {
	    		jsonData = jsonDataArray.getJSONObject(i);
	    		addToData(data.get(GROSS_MARGIN), jsonData.get("grossMargin"));
	    		addToData(data.get(NET_MARGIN), jsonData.get("netMargin"));
	    		addToData(data.get(OPERATING_MARGIN), jsonData.get("operatingMargin"));
	    		addToData(data.get(TAX_RATE), jsonData.get("taxRate"));
	    		addToData(data.get(ROA), jsonData.get("roa"));
	    		addToData(data.get(ROE), jsonData.get("roe"));
	    		addToData(data.get(ROIC), jsonData.get("roic"));
	    		addToData(data.get(INTEREST_COVERAGE_RATIO), jsonData.get("interestCoverage"));
	    		addToData(data.get(ASSET_TURNOVER), jsonData.get("assetsTurnover"));
			}
	    	roundAllValuesInData(data, null, 2);
		} catch (IOException e) {
			System.err.println("Error: cannot scrape Efficiency Data from Morningstar.com.");
		}
		return data;
	}
	
	/*
	 * Scrapes 'Financial Health' data from Morningstar.com.
	 * 
	 * @param url	URL of webpage to scrape data from.
	 * 
	 * @return Map object containing scraped data.
	 */
	public static Map<Metric, List<Double>> getFinancialHealthData(String url) {
		Document doc = null;
		JSONObject jsonBody, jsonData;
		JSONArray jsonDataArray;
		Map<Metric, List<Double>> data = new HashMap<>();
      	data.put(CURRENT_RATIO, new ArrayList<Double>());
      	data.put(DEBT_TO_EQUITY_RATIO, new ArrayList<Double>());
      	data.put(BVPS, new ArrayList<Double>());
		
		try {
			doc = Jsoup
					.connect(url)
					.userAgent(USER_AGENT)
					.header("apikey", API_KEY)
					.ignoreContentType(true)
					.timeout(60* 1000)
					.get();
			
	    	jsonBody = new JSONObject(doc.body().text());
	    	jsonDataArray = jsonBody.getJSONArray("dataList");
	    	
	    	for (int i = 0; i < 10; i++) {
	    		jsonData = jsonDataArray.getJSONObject(i);
	    		addToData(data.get(CURRENT_RATIO), jsonData.get("currentRatio"));
	    		addToData(data.get(DEBT_TO_EQUITY_RATIO), jsonData.get("debtEquityRatio"));
	    		addToData(data.get(BVPS), jsonData.get("bookValuePerShare"));
			}
	    	
	    	roundAllValuesInData(data, null, 2);
		} catch (IOException e) {
			System.err.println("Error: cannot scrape Financial Health Data from Morningstar.com.");
		}
		return data;
	}
	
	/*
	 * Scrapes 'Cashflow' data from Morningstar.com.
	 * 
	 * @param url	URL of webpage to scrape data from.
	 * 
	 * @return Map object containing scraped data.
	 */
	public static Map<Metric, List<Double>> getCashflowData(String url) {
		Document doc = null;
		JSONObject jsonBody, jsonData;
		JSONArray jsonDataArray;
		Map<Metric, List<Double>> data = new HashMap<>();
      	data.put(OPERATING_CASHFLOW_GROWTH, new ArrayList<Double>());
      	data.put(FREE_CASHFLOW_GROWTH, new ArrayList<Double>());
      	data.put(FREE_CASHFLOW_TO_REVENUE, new ArrayList<Double>());
      	data.put(FREE_CASHFLOW_TO_SHARES, new ArrayList<Double>());
      	data.put(CAP_EX_TO_REVENUE, new ArrayList<Double>());
      		
		try {
			doc = Jsoup
					.connect(url)
					.userAgent(USER_AGENT)
					.header("apikey", API_KEY)
					.ignoreContentType(true)
					.timeout(60* 1000)
					.get();
			
	    	jsonBody = new JSONObject(doc.body().text());
	    	jsonDataArray = jsonBody.getJSONArray("dataList");
	    	
	    	for (int i = 0; i < 10; i++) {
	    		jsonData = jsonDataArray.getJSONObject(i);
	    		addToData(data.get(OPERATING_CASHFLOW_GROWTH), jsonData.get("operatingCFGrowthPer"));
	    		addToData(data.get(FREE_CASHFLOW_GROWTH), jsonData.get("freeCashFlowGrowthPer"));
	    		addToData(data.get(FREE_CASHFLOW_TO_REVENUE), jsonData.get("freeCFPerSales"));
	    		addToData(data.get(FREE_CASHFLOW_TO_SHARES), jsonData.get("freeCashFlowPerShare"));
	    		addToData(data.get(CAP_EX_TO_REVENUE), jsonData.get("capExAsPerOfSales"));
			}
	    	roundAllValuesInData(data, FREE_CASHFLOW_TO_REVENUE, 2);
		} catch (IOException e) {
			System.err.println("Error: cannot scrape Cashflow Data from Morningstar.com.");
		}
		return data;
	}
	
	/*
	 * Scrapes 'Dividends' data from Morningstar.com.
	 * 
	 * @param url	URL of webpage to scrape data from.
	 * 
	 * @return Map object containing scraped data.
	 */
	public static Map<Metric, List<Double>> getDividendsData(String url) {
		Document doc = null;
		JSONObject jsonBody;
		JSONArray jsonDividendsDataArray, jsonPayoutRatioDataArray;
		Map<Metric, List<Double>> data = new HashMap<>();
		data.put(DIVIDENDS, new ArrayList<Double>());
		data.put(PAYOUT_RATIO, new ArrayList<Double>());
		
		try {
			doc = Jsoup
					.connect(url)
					.userAgent(USER_AGENT)
					.header("apikey", API_KEY)
					.ignoreContentType(true)
					.timeout(60* 1000)
					.get();
			
	    	jsonBody = new JSONObject(doc.body().text());
	    	jsonDividendsDataArray = jsonBody.getJSONArray("rows").getJSONObject(0).getJSONArray("datum");
	    	jsonPayoutRatioDataArray = jsonBody.getJSONArray("rows").getJSONObject(4).getJSONArray("datum");
	    	
	    	for (int i = 0; i < 10; i++) {
	    		addToData(data.get(DIVIDENDS), jsonDividendsDataArray.get(i));
	    		addToData(data.get(PAYOUT_RATIO), jsonPayoutRatioDataArray.get(i));
			}
	    	roundAllValuesInData(data, null, 2);
		} catch (IOException e) {
			System.err.println("Error: cannot scrape Dividends Data from Morningstar.com.");
		}
		return data;
	}
	
	/*
	 * Scrapes 'Financials' data from Morningstar.com.
	 * 
	 * @param url	URL of webpage to scrape data from.
	 * 
	 * @return Map object containing scraped data.
	 */
	public static Map<Metric, List<Double>> getFinancialsData(String url) {
		Document doc = null;
		JSONObject jsonBody;
		JSONArray revenueData, operatingIncomeData, netIncomeData, epsData, operatingCashflowData,
		freeCashflowData;
		Map<Metric, List<Double>> data = new HashMap<>();
		data.put(REVENUE, new ArrayList<>());
		data.put(OPERATING_INCOME, new ArrayList<>());
		data.put(NET_INCOME, new ArrayList<>());
		data.put(EPS, new ArrayList<>());
		data.put(OPERATING_CASHFLOW, new ArrayList<>());
		data.put(FREE_CASHFLOW, new ArrayList<>());

		try {
			doc = Jsoup
					.connect(url)
					.userAgent(USER_AGENT)
					.header("apikey", API_KEY)
					.ignoreContentType(true)
					.timeout(60 * 1000)
					.get();
			
	    	jsonBody = new JSONObject(doc.body().text());
	    	revenueData = jsonBody.getJSONObject("incomeStatement").getJSONArray("rows")
	    			.getJSONObject(0).getJSONArray("datum");
	    	operatingIncomeData = jsonBody.getJSONObject("incomeStatement").getJSONArray("rows")
	    			.getJSONObject(1).getJSONArray("datum");
	    	netIncomeData = jsonBody.getJSONObject("incomeStatement").getJSONArray("rows")
	    			.getJSONObject(2).getJSONArray("datum");
	    	epsData = jsonBody.getJSONObject("incomeStatement").getJSONArray("rows")
	    			.getJSONObject(5).getJSONArray("datum");
	    	operatingCashflowData = jsonBody.getJSONObject("cashFlow").getJSONArray("rows")
	    			.getJSONObject(0).getJSONArray("datum");
	    	freeCashflowData = jsonBody.getJSONObject("cashFlow").getJSONArray("rows")
	    			.getJSONObject(4).getJSONArray("datum");
	    	
	    	addToData(data.get(REVENUE), revenueData.get(revenueData.length() - 2));
	    	addToData(data.get(OPERATING_INCOME), operatingIncomeData.get(operatingIncomeData.length() - 2));
	    	addToData(data.get(NET_INCOME), netIncomeData.get(netIncomeData.length() - 2));
	    	addToData(data.get(OPERATING_CASHFLOW), operatingCashflowData.get(operatingCashflowData.length() - 2));
	    	addToData(data.get(FREE_CASHFLOW), freeCashflowData.get(freeCashflowData.length() - 2));
	    	roundAllValuesInData(data, null, 0);
	    	
	    	addToData(data.get(EPS), epsData.get(epsData.length() - 2));
	    	roundAllValuesInData(data, EPS, 2);
		} catch (IOException e) {
			System.err.println("Error: cannot scrape Financials Data from Morningstar.com.");
		}
		return data;
	}
	
	/*
	 * Adds data to a List. Added value is checked for correct data type.
	 * 
	 * @param list			List in which the value is added.
	 * @param valueToAdd	value to add.
	 */
	private static void addToData(List<Double> list, Object valueToAdd) {
		try {
			list.add(Double.parseDouble(valueToAdd.toString()));
		} catch (NumberFormatException nfe) {
			list.add(Double.NaN);
		}
	}
	
	/*
	 * Rounds all values in a map.
	 * 
	 * @param data			Map object containing the data to round.
	 * @param name			metric name.
	 * @param digits		number of digits specifying the precision.
	 */
	private static void roundAllValuesInData(Map<Metric, List<Double>> data, Metric name, int digits) {
		List<Double> roundedValues;
		
		if (name == null) {
			for (Metric dataKey : data.keySet()) {
				roundedValues = new ArrayList<>();
				for (double value : data.get(dataKey)) {
					if (value != Double.NaN) {
						roundedValues.add(CalculationUtils.roundToXDecimals(value, digits));
					} else {
						roundedValues.add(Double.NaN);
					}
				}
				data.put(dataKey, roundedValues);
			}
		} else {
			roundedValues = new ArrayList<>();
			for (double value : data.get(name)) {
				if (value != Double.NaN) {
					roundedValues.add(CalculationUtils.roundToXDecimals(value, digits));
				} else {
					roundedValues.add(Double.NaN);
				}
				data.put(name, roundedValues);
			}
		}
	}
}
