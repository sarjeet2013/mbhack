package com.mbhack.payload;

public class PlaceBuilderData {
	private String name;
	private String lat;
	private String longi;
	
	public PlaceBuilderData(String name, String lat, String longi) {
		this.name = name;
		this.lat = lat;
		this.longi = longi;
	}
	
	public String getName() {
		return name;
	}
	
	public String getLat() {
		return lat;
	}
	
	public String getLongi() {
		return longi;
	}

}
