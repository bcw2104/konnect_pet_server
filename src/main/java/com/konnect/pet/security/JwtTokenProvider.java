package com.konnect.pet.security;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenProvider {

	private final Key key;

	private final CustomUserDetailsService customUserDetailsService;

	private final UserRepository userRepository;

	public JwtTokenProvider(@Value("${jwt.secret}") String secretKey, CustomUserDetailsService customUserDetailsService,
			UserRepository userRepository) {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		this.key = Keys.hmacShaKeyFor(keyBytes);
		this.customUserDetailsService = customUserDetailsService;
		this.userRepository = userRepository;
	}

	// 유저 정보를 가지고 AccessToken, RefreshToken 을 생성하는 메서드
	@Transactional
	public TokenInfoDto generateToken(Long userId, String role) {

		Optional<User> userOpt = userRepository.findById(userId);

		if(userOpt.isEmpty()) {
			throw new CustomResponseException(ResponseType.AUTH_FAIL);
		}

		User user = userOpt.get();

		String tokenId = RandomStringUtils.randomAlphabetic(6);

		Date now = new Date();
		long timestamp = (new Date()).getTime();
		// Access Token 생성 - 하루
		Date accessTokenExpiresIn = new Date(timestamp + 86400000);
		String accessToken = Jwts.builder()
				.setSubject(user.getEmail())
				.claim("role", user.getAuthorities())
				.claim("id", tokenId)
				.setExpiration(accessTokenExpiresIn).
				setIssuedAt(now)
				.signWith(key, SignatureAlgorithm.HS256).compact();


		// Refresh Token 생성 - 1년
		String refreshToken = Jwts.builder()
				.setSubject(tokenId)
				.setExpiration(new Date(timestamp + (86400000 * 365))).setIssuedAt(now)
				.signWith(key, SignatureAlgorithm.HS256).compact();


		return TokenInfoDto.builder().grantType("Bearer").accessToken(accessToken).refreshToken(refreshToken).build();
	}

	// AccessToken 만료 시 , RefreshToken 을 생성하는 메서드
	public TokenInfoDto generateNewAccessTokenByRefreshToken(String accessToken, String refreshToken) {
		// 토큰 복호화
		Claims claims = parseClaims(accessToken);

		if (claims.get("role") == null) {
			throw new RuntimeException("권한 정보가 없는 토큰입니다.");
		}

		String newAccessToken = null;
		if (refreshToken != null && validateToken(refreshToken)) {
			long now = (new Date()).getTime();
			// Access Token 생성 - 하루
			Date accessTokenExpiresIn = new Date(now + 86400000);
			newAccessToken = Jwts.builder()
					.setSubject(claims.getSubject())
					.claim("role", claims.get("role"))
					.setExpiration(accessTokenExpiresIn)
					.signWith(key, SignatureAlgorithm.HS256).compact();
		} else {
			throw new CustomResponseException(ResponseType.INVALID_REFRESH_TOKEN);
		}

		return TokenInfoDto.builder().grantType("Bearer").accessToken(newAccessToken).refreshToken(refreshToken)
				.build();
	}

	// JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
	public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get("role") == null) {
            throw new CustomResponseException(ResponseType.INVALID_ACCESS_TOKEN);
        }

        String username = claims.getSubject();

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("role").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = customUserDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

	// 토큰 정보를 검증하는 메서드
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (SecurityException | MalformedJwtException e) {
			log.info("Invalid JWT Token", e);
		} catch (ExpiredJwtException e) {
			log.info("Expired JWT Token", e);
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
}