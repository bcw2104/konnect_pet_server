package com.konnect.pet.security;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.konnect.pet.dto.TokenInfoDto;
import com.konnect.pet.entity.User;
import com.konnect.pet.enums.ResponseType;
import com.konnect.pet.ex.CustomResponseException;
import com.konnect.pet.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenProvider {

	private final Key key;

	private final CustomUserDetailsService customUserDetailsService;

	private UserRepository userRepository;

	private Long ACCESS_TOKEN_LIFE = 1000L * 60L * 60L * 24L;
	private Long REFRESH_TOKEN_LIFE = 1000L * 60L * 60L * 24L * 365L;

	public JwtTokenProvider(@Value("${jwt.secret}") String secretKey, CustomUserDetailsService customUserDetailsService,
			UserRepository userRepository) {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		this.key = Keys.hmacShaKeyFor(keyBytes);
		this.customUserDetailsService = customUserDetailsService;
		this.userRepository = userRepository;
	}

	// 유저 정보를 가지고 AccessToken, RefreshToken 을 생성하는 메서드
	@Transactional
	public TokenInfoDto generateToken(Long userId) {

		Optional<User> userOpt = userRepository.findById(userId);

		if (userOpt.isEmpty()) {
			throw new CustomResponseException(ResponseType.AUTH_FAIL);
		}

		User user = userOpt.get();

		String tokenId = RandomStringUtils.randomAlphabetic(6);

		Date now = new Date();
		long timestamp = (new Date()).getTime();
		// Access Token 생성 - 하루
		Date accessTokenExpireAt = new Date(timestamp + ACCESS_TOKEN_LIFE);
		String accessToken = Jwts.builder().setSubject(user.getId().toString()).claim("id", tokenId)
				.setExpiration(accessTokenExpireAt).setIssuedAt(now).signWith(key, SignatureAlgorithm.HS256).compact();

		// Refresh Token 생성 - 1년
		Date refreshTokenExpireAt = new Date(timestamp + REFRESH_TOKEN_LIFE);
		String refreshToken = Jwts.builder().setSubject(tokenId).setExpiration(refreshTokenExpireAt).setIssuedAt(now)
				.signWith(key, SignatureAlgorithm.HS256).compact();

		user.setAuthTokenId(tokenId);

		log.info("Generate User Token - userId: {}", user.getId());
		return TokenInfoDto.builder().grantType("Bearer").accessToken(accessToken).refreshToken(refreshToken)
				.accessTokenExpireAt(accessTokenExpireAt.getTime()).refreshTokenExpireAt(refreshTokenExpireAt.getTime())
				.build();
	}

	// AccessToken 만료 시 , RefreshToken 을 생성하는 메서드
	@Transactional
	public TokenInfoDto generateTokenByRefreshToken(String accessToken, String refreshToken) {
		// 토큰 복호화
		Claims accessClaims = parseClaims(accessToken);
		Claims refreshClaims = parseClaims(refreshToken);

		Optional<User> userOpt = userRepository.findById(Long.parseLong(accessClaims.getSubject()));

		if (userOpt.isEmpty()) {
			throw new CustomResponseException(ResponseType.INVALID_ACCESS_TOKEN);
		}

		User user = userOpt.get();

		if (refreshToken != null && validateToken(refreshToken)
				&& refreshClaims.getSubject().equals(user.getAuthTokenId())
				&& refreshClaims.getSubject().equals(accessClaims.get("id"))) {

			String tokenId = RandomStringUtils.randomAlphabetic(6);

			Date now = new Date();
			long timestamp = (new Date()).getTime();
			// Access Token 생성 - 하루
			Date accessTokenExpireAt = new Date(timestamp + ACCESS_TOKEN_LIFE);
			String newAccessToken = Jwts.builder().setSubject(user.getId().toString()).claim("id", tokenId)
					.setExpiration(accessTokenExpireAt).setIssuedAt(now).signWith(key, SignatureAlgorithm.HS256)
					.compact();

			// Refresh Token 생성 - 1년
			Date refreshTokenExpireAt = new Date(timestamp + REFRESH_TOKEN_LIFE);
			String newRefreshToken = Jwts.builder().setSubject(tokenId).setExpiration(refreshTokenExpireAt)
					.setIssuedAt(now).signWith(key, SignatureAlgorithm.HS256).compact();

			user.setAuthTokenId(tokenId);

			log.info("ReGenerate User Token - userId: {}", user.getId());
			return TokenInfoDto.builder().grantType("Bearer").accessToken(newAccessToken).refreshToken(newRefreshToken)
					.accessTokenExpireAt(accessTokenExpireAt.getTime())
					.refreshTokenExpireAt(refreshTokenExpireAt.getTime()).build();
		} else {
			throw new CustomResponseException(ResponseType.INVALID_REFRESH_TOKEN);
		}
	}

	// JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
	public Authentication getAuthentication(String accessToken) {
		// 토큰 복호화
		Claims claims = parseClaims(accessToken);

		String username = claims.getSubject();
		String tokenId = claims.get("id").toString();

		// UserDetails 객체를 만들어서 Authentication 리턴
		UserDetails principal = customUserDetailsService.loadUserByUsername(username);

		User user = (User) principal;

		if (!tokenId.equals(user.getAuthTokenId())) {
			throw new AccessDeniedException("invalid token");
		}
		return new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
	}

	// 토큰 정보를 검증하는 메서드
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (SecurityException | MalformedJwtException e) {
			log.info("Invalid JWT Token", e);
		} catch (ExpiredJwtException e) {
			log.info("Expired JWT Token", e.getMessage());
			throw new CustomResponseException(HttpStatus.FORBIDDEN, ResponseType.EXPIRED_ACCESS_TOKEN);
		} catch (UnsupportedJwtException e) {
			log.info("Unsupported JWT Token", e);
		} catch (IllegalArgumentException e) {
			log.info("JWT claims string is empty.", e);
		}
		return false;
	}

	private Claims parseClaims(String accessToken) {
		try {
			return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
	}

	// Request Header 에서 토큰 정보 추출
	public String resolveToken(HttpServletRequest request, String header) {
		String bearerToken = request.getHeader(header);
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
			return bearerToken.substring(7);
		}
		return null;
	}
}