package de.personal.value_investing.database;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.personal.value_investing.utils.DatabaseUtils;
import de.personal.value_investing.utils.Metric;

public class Database {
	private String stockTicker;
	private String exchangeTicker;
	private Map<Metric, List<Double>> data;
	
	public Database(String stockTicker, String exchangeTicker) {
		this.stockTicker = stockTicker;
		this.exchangeTicker = exchangeTicker;
		this.data = DatabaseUtils.buildCompleteDatabase(exchangeTicker, stockTicker);
	}
	
	public Database(String stockTicker, String exchangeTicker, Map<Metric, List<Double>> data) {
		this.stockTicker = stockTicker;
		this.exchangeTicker = exchangeTicker;
		this.data = data;
	}

	public String getStockTicker() {
		return this.stockTicker;
	}

	public String getExchangeTicker() {
		return this.exchangeTicker;
	}

	public Map<Metric, List<Double>> getData() {
		return this.data;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("Morningstar Data on the %s Stock\n", stockTicker));
		for (Entry<Metric, List<Double>> entry : data.entrySet()) {
			sb.append(String.format("%-30s |", entry.getKey().getNameAndNotation()));
			for (double val : entry.getValue()) {
				sb.append(String.format("%-12.2f |", val));
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}
