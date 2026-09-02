package com.example.productmanagement.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

	@Value("${security.jwt.secret}")
	private String secret;

	@Value("${security.jwt.expiration}")
	private long jwtExpiration;

	/*
	 * Generate JWT access token.
	 */
	public String generateToken(UserDetails userDetails) {

		return Jwts.builder().subject(userDetails.getUsername())
				.claim("role", userDetails.getAuthorities().iterator().next().getAuthority()).issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + jwtExpiration)).signWith(getSigningKey()).compact();
	}

	/*
	 * Extract username from JWT.
	 */
	public String extractUsername(String token) {

		return extractClaim(token, Claims::getSubject);
	}

	/*
	 * Validate JWT.
	 */
	public boolean isTokenValid(String token, UserDetails userDetails) {

		String username = extractUsername(token);

		return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}

	/*
	 * Check token expiration.
	 */
	private boolean isTokenExpired(String token) {

		return extractExpiration(token).before(new Date());
	}

	/*
	 * Extract expiration.
	 */
	private Date extractExpiration(String token) {

		return extractClaim(token, Claims::getExpiration);
	}

	/*
	 * Extract generic claim.
	 */
	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {

		Claims claims = Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();

		return claimsResolver.apply(claims);
	}

	/*
	 * Generate signing key.
	 */
	private SecretKey getSigningKey() {

		byte[] keyBytes = Decoders.BASE64.decode(secret);

		return Keys.hmacShaKeyFor(keyBytes);
	}
}