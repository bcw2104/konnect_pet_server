package com.konnect.pet.security;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.konnect.pet.dto.JwtTokenDto;
import com.konnect.pet.entity.User;
import com.konnect.pet.entity.UserDailyAccessLog;
import com.konnect.pet.entity.UserPointHistory;
import com.konnect.pet.entity.redis.RefreshToken;
import com.konnect.pet.enums.ResponseType;
import com.konnect.pet.enums.code.PointHistoryTypeCode;
import com.konnect.pet.ex.CustomResponseException;
import com.konnect.pet.repository.UserDailyAccessLogRepository;
import com.konnect.pet.repository.UserPointHistoryRepository;
import com.konnect.pet.repository.UserRepository;
import com.konnect.pet.repository.redis.RefreshTokenRepository;
import com.konnect.pet.service.EventService;
import com.konnect.pet.utils.ValidationUtils;

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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenProvider {

	private final Key key;

	private final CustomUserDetailsService customUserDetailsService;

	private final UserRepository userRepository;
	private final UserPointHistoryRepository userPointHistoryRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final UserDailyAccessLogRepository userDailyAccessLogRepository;
	private final EventService eventService;

	@Value("${application.jwt.access.exp}")
	private Long ACCESS_TOKEN_EXP;
	@Value("${application.jwt.refresh.exp}")
	private Long REFRESH_TOKEN_EXP;

	public JwtTokenProvider(@Value("${application.jwt.secret}") String TOKEN_SECRET,
			CustomUserDetailsService customUserDetailsService, RefreshTokenRepository refreshTokenRepository,
			UserRepository userRepository, UserPointHistoryRepository userPointHistoryRepository,
			UserDailyAccessLogRepository userDailyAccessLogRepository,
			EventService eventService) {
		this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(TOKEN_SECRET));
		this.customUserDetailsService = customUserDetailsService;
		this.refreshTokenRepository = refreshTokenRepository;
		this.userRepository = userRepository;
		this.userPointHistoryRepository = userPointHistoryRepository;
		this.eventService = eventService;
		this.userDailyAccessLogRepository = userDailyAccessLogRepository;
	}

	// 유저 정보를 가지고 AccessToken, RefreshToken 을 생성하는 메서드
	@Transactional
	public JwtTokenDto generateToken(Long userId) {

		refreshTokenRepository.deleteById(userId);

		String atkId = ValidationUtils.generateRandomString(6, true, true);
		Date now = new Date();
		long timestamp = (new Date()).getTime();
		// Access Token 생성
		Date accessTokenExpireAt = new Date(timestamp + ACCESS_TOKEN_EXP * 1000L);
		String accessToken = Jwts.builder().setSubject("ATK").claim("id", userId).claim("atkId", atkId)
				.setExpiration(accessTokenExpireAt).setIssuedAt(now).signWith(key, SignatureAlgorithm.HS256).compact();

		// Refresh Token 생성
		Date refreshTokenExpireAt = new Date(timestamp + REFRESH_TOKEN_EXP * 1000L);
		String refreshToken = Jwts.builder().setSubject("RTK").claim("id", userId).setExpiration(refreshTokenExpireAt)
				.setIssuedAt(now).signWith(key, SignatureAlgorithm.HS256).compact();

		RefreshToken redisRefreshToken = new RefreshToken(userId, refreshToken, REFRESH_TOKEN_EXP);
		refreshTokenRepository.save(redisRefreshToken);

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new CustomResponseException(ResponseType.INVALID_PARAMETER));

		user.setAktId(atkId);

		log.info("Generate User Token - userId: {}", userId);

		int dailyCount = userDailyAccessLogRepository.countAfterByUserId(user.getId(), LocalDateTime.now().with(LocalTime.MIDNIGHT));
		if (dailyCount == 0) {
			eventService.provideEventReward(user, PointHistoryTypeCode.ATTENDANCE);

			UserDailyAccessLog accessLog = new UserDailyAccessLog();
			accessLog.setUser(user);
			accessLog.setDeviceModel(user.getDeviceModel());
			accessLog.setDeviceOs(user.getDeviceOs());
			accessLog.setDeviceOsVersion(user.getDeviceOsVersion());
			userDailyAccessLogRepository.save(accessLog);
		}

		return JwtTokenDto.builder().grantType("Bearer").accessToken(accessToken).refreshToken(refreshToken)
				.accessTokenExpireAt(accessTokenExpireAt.getTime()).refreshTokenExpireAt(refreshTokenExpireAt.getTime())
				.build();
	}

	// JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
	public Authentication getAuthentication(String token) {
		// 토큰 복호화
		Claims claims = parseClaims(token);

		String username = claims.get("id").toString();
		String subject = claims.getSubject();

		// UserDetails 객체를 만들어서 Authentication 리턴
		UserDetails principal = customUserDetailsService.loadUserByUsername(username);

		if (subject.equals("ATK")) {
			String atkId = claims.get("atkId").toString();

			User user = (User) principal;
			if (user.getId() != null && !atkId.equals(user.getAktId())) {
				throw new UsernameNotFoundException("user login other device");
			}
		}

		return new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
	}

	// 토큰 정보를 검증하는 메서드
	public ResponseType validateToken(String token, String requestURI) {
		boolean isRefesh = requestURI.equals("/api/v1/auth/token/refresh");

		try {
			Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
			String subject = claims.getSubject();

			if ((isRefesh && subject.equals("RTK")) || (!isRefesh && subject.equals("ATK"))) {
				return ResponseType.SUCCESS;
			}
		} catch (SecurityException | MalformedJwtException e) {
			log.info("Invalid JWT Token", e);
		} catch (ExpiredJwtException e) {
			log.info("Expired JWT Token", e);
			return isRefesh ? ResponseType.INVALID_REFRESH_TOKEN : ResponseType.INVALID_ACCESS_TOKEN;
		} catch (UnsupportedJwtException e) {
			log.info("Unsupported JWT Token", e);
		} catch (IllegalArgumentException e) {
			log.info("JWT claims string is empty.", e);
		}
		return ResponseType.AUTH_FAIL;
	}

	public Claims parseClaims(String token) {
		try {
			return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
	}

	// Request Header 에서 토큰 정보 추출
	public String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
			return bearerToken.substring(7);
		}
		return null;
	}
}