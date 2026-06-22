package com.example.weather.service;

import com.example.weather.model.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class WeatherService {

	private final RestClient restClient;

	@Value("${openweathermap.api-key}")
	private String apiKey;

	@Cacheable(value = "weather", key = "#lat + '-' + #lon")
	public Root getWeather(double lat, double lon) {
		try {
			return restClient.get()
					.uri("https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={appid}&units=metric",
							lat, lon, apiKey)
					.retrieve()
					.body(Root.class);
		} catch (RestClientResponseException ex) {
			if (ex.getStatusCode().value() == 401) {
				throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
						"Неверный API-ключ OpenWeatherMap. Проверьте openweathermap.api-key в application.properties "
								+ "(новый ключ может активироваться до 2 часов).");
			}
			throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
					"Ошибка OpenWeatherMap API: " + ex.getStatusCode().value());
		}
	}

}
