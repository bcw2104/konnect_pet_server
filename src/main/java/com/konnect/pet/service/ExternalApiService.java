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

	@Value("${social.auth.google.end-point}")
	private String GOOGLE_BASE_URL;
	@Value("${social.auth.facebook.end-point}")
	private String FACEBOOK_BASE_URL;

	public Map callGoogleUserInfoApi(String token) throws IOException {
		log.info("{}", GOOGLE_BASE_URL+"/userinfo/v2/me");
		
		Mono<Map> response = webClient.get().uri(GOOGLE_BASE_URL+"/userinfo/v2/me")
				.header("Authorization", "Bearer" + token)
				.retrieve()
				.onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
						clientResponse -> clientResponse.bodyToMono(String.class)
								.map(body -> new IOException(body)))
				.bodyToMono(Map.class);

		return response.block();
	}
	
	public Map callFacebookUserInfoApi(String token) throws IOException {
		log.info("{}",FACEBOOK_BASE_URL+"/me");
		
		Mono<Map> response = webClient.get().uri(FACEBOOK_BASE_URL+"/me",
				uriBuilder ->
					uriBuilder
					.queryParam("access_token", token)
					.queryParam("fields", "id,name,email")
					.build())
				.retrieve()
				.onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
						clientResponse -> clientResponse.bodyToMono(String.class)
						.map(body -> new IOException(body)))
				.bodyToMono(Map.class);
		
		return response.block();
	}
}
