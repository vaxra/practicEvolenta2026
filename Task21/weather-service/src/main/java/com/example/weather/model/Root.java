package com.example.weather.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Root {

	public Coord coord;
	public ArrayList<Weather> weather;
	public String base;
	public Main main;
	public int visibility;
	public Wind wind;
	public Clouds clouds;
	public int dt;
	public Sys sys;
	public int timezone;
	public int id;
	public String name;
	public int cod;

}
