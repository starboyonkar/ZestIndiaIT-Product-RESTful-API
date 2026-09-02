package com.example.productmanagement.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.productmanagement.dto.auth.AuthResponse;
import com.example.productmanagement.dto.auth.LoginRequest;
import com.example.productmanagement.dto.auth.RefreshTokenRequest;
import com.example.productmanagement.dto.auth.RegisterRequest;
import com.example.productmanagement.entity.RefreshToken;
import com.example.productmanagement.entity.User;
import com.example.productmanagement.enums.Role;
import com.example.productmanagement.exception.BadRequestException;
import com.example.productmanagement.repository.UserRepository;
import com.example.productmanagement.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	private final AuthenticationManager authenticationManager;

	private final RefreshTokenService refreshTokenService;

	private final JwtService jwtService;

	private final UserDetailsService userDetailsService;

	/**
	 * Register user.
	 */
	public void register(RegisterRequest request) {

		if (userRepository.existsByUsername(request.getUsername())) {

			throw new BadRequestException("Username already exists");
		}

		if (userRepository.existsByEmail(request.getEmail())) {

			throw new BadRequestException("Email already registered");
		}

		User user = User.builder().username(request.getUsername().trim()).email(request.getEmail().trim().toLowerCase())
				.password(passwordEncoder.encode(request.getPassword())).role(Role.USER).enabled(true).build();

		userRepository.save(user);
	}

	public AuthResponse login(LoginRequest request) {

		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

		UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

		String accessToken = jwtService.generateToken(userDetails);

		return AuthResponse.builder().accessToken(accessToken).tokenType("Bearer").expiresIn(900).build();
	}

	public AuthResponse refreshToken(RefreshTokenRequest request) {

		RefreshToken oldToken = refreshTokenService.validateRefreshToken(request.getRefreshToken());

		User user = oldToken.getUser();

		UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

		String newAccessToken = jwtService.generateToken(userDetails);

		RefreshToken newRefreshToken = refreshTokenService.rotateRefreshToken(oldToken);

		return AuthResponse.builder().accessToken(newAccessToken).refreshToken(newRefreshToken.getToken())
				.tokenType("Bearer").expiresIn(900L).build();
	}

	public void logout(String refreshToken) {

		refreshTokenService.revokeToken(refreshToken);
	}
}