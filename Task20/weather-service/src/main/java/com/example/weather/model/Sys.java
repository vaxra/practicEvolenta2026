package com.example.weather.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Sys {

	private String country;
	private int sunrise;
	private int sunset;

}
