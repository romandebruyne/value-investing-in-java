package de.personal.value_investing.utils;

public enum Region {
	AFRICA("Africa"),
	ASIA("Asia"),
	AUSTRALIA_AND_NEW_ZEALAND("Australia & New Zealand"),
	CARIBBEAN("Caribbean"),
	CENTRAL_AND_SOUTH_AMERICA("Central and South America"),
	EASTERN_EUROPE_AND_RUSSIA("Eastern Europe & Russia"),
	MIDDLE_EAST("Middle East"),
	NORTH_AMERICA("North America"),
	WESTERN_EUROPE("Western Europe"),
	INVALID("Invalid region");
	
	private String region;
	
	private Region(String region) {
		this.region = region;
	}
	
	public String getRegion() {
		return this.region;
	}
	
	public static Region fromString(String str) {
		switch (str) {
		case "Africa":
			return AFRICA;
		case "Asia":
			return ASIA;
		case "Australia & New Zealand":
			return AUSTRALIA_AND_NEW_ZEALAND;
		case "Caribbean":
			return CARIBBEAN;
		case "Central and South America":
			return CENTRAL_AND_SOUTH_AMERICA;
		case "Eastern Europe & Russia":
			return EASTERN_EUROPE_AND_RUSSIA;
		case "Middle East":
			return MIDDLE_EAST;
		case "North America":
			return NORTH_AMERICA;
		case "Western Europe":
			return WESTERN_EUROPE;
		default:
			return INVALID;
		}
	}
}
