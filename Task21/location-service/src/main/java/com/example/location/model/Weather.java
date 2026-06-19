package com.example.location.model;

public class Weather {

	private Double temp;
	private String description;
	private Integer humidity;

	public Weather() {
	}

	public Weather(Double temp, String description, Integer humidity) {
		this.temp = temp;
		this.description = description;
		this.humidity = humidity;
	}

	public Double getTemp() {
		return temp;
	}

	public void setTemp(Double temp) {
		this.temp = temp;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getHumidity() {
		return humidity;
	}

	public void setHumidity(Integer humidity) {
		this.humidity = humidity;
	}

}
