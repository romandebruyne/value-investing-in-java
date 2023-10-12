package de.personal.value_investing.utils;

public enum Period {
	ONE_YEAR(1),
	THREE_YEARS(3),
	TEN_YEARS(10);
	
	private int period;
	
	private Period(int period) {
		this.period = period;
	}
	
	public int getPeriod() {
		return this.period;
	}

}
