package com.example.weather.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Main {

	private double temp;
	@JsonProperty("feels_like")
	private double feels_like;
	@JsonProperty("temp_min")
	private double temp_min;
	@JsonProperty("temp_max")
	private double temp_max;
	private int pressure;
	private int humidity;
	@JsonProperty("sea_level")
	private int sea_level;
	@JsonProperty("grnd_level")
	private int grnd_level;

}
