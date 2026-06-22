package com.example.weather.config;

import com.example.weather.model.Root;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class WeatherConfig {

	@Bean
	public CacheManager cacheManager() {
		CaffeineCacheManager cacheManager = new CaffeineCacheManager("weather");
		cacheManager.setCaffeine(Caffeine.newBuilder()
				.expireAfterWrite(1, TimeUnit.MINUTES)
				.maximumSize(500));
		return cacheManager;
	}

	@Bean
	public RestClient restClient() {
		return RestClient.create();
	}

	@Bean
	public WeatherApiService weatherApiService(RestClient restClient,
			@Value("${openweathermap.api-key}") String apiKey) {
		return new WeatherApiService(restClient, apiKey);
	}

	@RequiredArgsConstructor
	public static class WeatherApiService {

		private final RestClient restClient;
		private final String apiKey;

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

}
