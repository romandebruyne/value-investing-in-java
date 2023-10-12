package de.personal.value_investing.utils;

public enum DataInterval {
	ONE_MINUTE("1m"),
	TWO_MINUTES("2m"),
	FIVE_MINUTES("5m"),
	FIFTEEN_MINUTES("15m"),
	THIRTY_MINUTES("30m"),
	SIXTY_MINUTES("60m"),
	NINETY_MINUTES("90m"),
	ONE_HOUR("1h"),
	ONE_DAY("1d"),
	FIVE_DAYS("5d"),
	ONE_WEEK("1wk"),
	ONE_MONTH("1mo"),
	THREE_MONTHS("1mo");
	
	private String interval;
	
	private DataInterval(String interval) {
		this.interval = interval;
	}
	
	public String getInterval() {
		return this.interval;
	}
}
