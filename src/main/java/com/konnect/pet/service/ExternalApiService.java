package com.konnect.pet.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalApiService {

	private final WebClient webClient;

	@Value("${social.auth.google.user.info}")
	private String GOOGLE_USER_INFO_URL;

	public Map callGoogleUserInfoApi(String token) throws IOException {

		log.info("{}",GOOGLE_USER_INFO_URL);
		Mono<Map> response = webClient.get().uri(GOOGLE_USER_INFO_URL).header("Authorization", "Bearer" + token)
				.retrieve()
				.onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
						clientResponse -> clientResponse.bodyToMono(String.class)
								.map(body -> new IOException(body)))
				.bodyToMono(Map.class);

		return response.flux().toStream().findFirst().orElse(null);
	}
}
