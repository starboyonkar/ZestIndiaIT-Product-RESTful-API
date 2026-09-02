package com.example.productmanagement.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.productmanagement.entity.RefreshToken;
import com.example.productmanagement.entity.User;
import com.example.productmanagement.exception.BadRequestException;
import com.example.productmanagement.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService {

	private final RefreshTokenRepository refreshTokenRepository;

	@Value("${security.jwt.refresh-expiration}")
	private long refreshExpiration;

	/*
	 * Create refresh token.
	 */
	public RefreshToken createRefreshToken(User user) {

		RefreshToken refreshToken = RefreshToken.builder().token(UUID.randomUUID().toString()).user(user)
				.expiresAt(LocalDateTime.now().plusSeconds(refreshExpiration / 1000)).revoked(false).used(false)
				.build();

		return refreshTokenRepository.save(refreshToken);
	}

	/*
	 * Validate refresh token.
	 */
	public RefreshToken validateRefreshToken(String token) {

		RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
				.orElseThrow(() -> new BadRequestException("Invalid refresh token"));

		if (refreshToken.isRevoked()) {
			throw new BadRequestException("Refresh token has been revoked");
		}

		if (refreshToken.isUsed()) {
			throw new BadRequestException("Refresh token has already been used");
		}

		if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {

			throw new BadRequestException("Refresh token has expired");
		}

		return refreshToken;
	}

	/*
	 * Rotate refresh token.
	 */
	public RefreshToken rotateRefreshToken(RefreshToken oldToken) {

		oldToken.setUsed(true);
		oldToken.setRevoked(true);

		refreshTokenRepository.save(oldToken);

		return createRefreshToken(oldToken.getUser());
	}

	/*
	 * Revoke all refresh tokens for user.
	 */
	public void revokeAllUserTokens(Long userId) {

		refreshTokenRepository.deleteByUserId(userId);
	}

	public void revokeToken(String token) {

		RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
				.orElseThrow(() -> new BadRequestException("Invalid refresh token"));

		refreshToken.setRevoked(true);

		refreshTokenRepository.save(refreshToken);
	}
}