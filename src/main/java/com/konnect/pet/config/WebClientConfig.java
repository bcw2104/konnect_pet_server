package com.konnect.pet.config;

import java.time.Duration;

import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {
	private final int CONNECT_TIMEOUT = 10000;
	private final int RESPONSE_TIMEOUT = 5000;
	private final int MAX_MEMORY_SIZE = 10 * 1024 * 1024;

	@Bean
	public WebClient webClient() {
		HttpClient client = HttpClient.create().option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECT_TIMEOUT)
				.responseTimeout(Duration.ofMillis(RESPONSE_TIMEOUT)).doOnConnected(conn -> conn
						.addHandlerLast(new ReadTimeoutHandler(5)).addHandlerLast(new WriteTimeoutHandler(5)));

		ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
				.codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(MAX_MEMORY_SIZE)).build();

		return WebClient.builder().clientConnector(new ReactorClientHttpConnector(client))
				.exchangeStrategies(exchangeStrategies).build();
	}
}
