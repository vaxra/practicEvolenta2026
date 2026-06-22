package com.example.weather.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Main {

	public double temp;
	@JsonProperty("feels_like")
	public double feels_like;
	@JsonProperty("temp_min")
	public double temp_min;
	@JsonProperty("temp_max")
	public double temp_max;
	public int pressure;
	public int humidity;
	@JsonProperty("sea_level")
	public int sea_level;
	@JsonProperty("grnd_level")
	public int grnd_level;

}
