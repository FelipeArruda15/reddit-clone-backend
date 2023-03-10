package com.felipearruda.redditclone.service;

import com.felipearruda.redditclone.config.JWTUtils;
import com.felipearruda.redditclone.dto.AuthenticationResponse;
import com.felipearruda.redditclone.dto.LoginRequest;
import com.felipearruda.redditclone.dto.RefreshTokenRequest;
import com.felipearruda.redditclone.dto.RegisterRequest;
import com.felipearruda.redditclone.exception.SpringRedditException;
import com.felipearruda.redditclone.model.*;
import com.felipearruda.redditclone.repository.UserRepository;
import com.felipearruda.redditclone.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static com.felipearruda.redditclone.util.Constants.ACTIVATION_EMAIL;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JWTUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public void signup(RegisterRequest registerRequest) {
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setUsername(registerRequest.getUsername());
        user.setPassword(encodePassword(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setActivated(false);

        userRepository.save(user);

        String token = generateVerificationToken(user);

        emailService.sendEmail(new NotificationEmail("Por favor, ative sua conta ", user.getEmail(),
                "Obrigado por se inscrever no Spring Reddit, clique na URL abaixo para ativar sua conta: "
                + ACTIVATION_EMAIL + token));
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new SpringRedditException("Token inv??lido"));
        fetchUserAndEnable(verificationToken.get());
    }

    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken){
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new SpringRedditException("Usu??rio n??o encontrado"));
        user.setActivated(true);
        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()));

        UserDetails user = userDetailsService.loadUserByUsername(loginRequest.getUsername());

        if (user != null){
            String authenticationToken = jwtUtils.generateToken(user);
            return AuthenticationResponse.builder()
                    .authenticationToken(authenticationToken)
                    .expiresAt(Instant.now().plusMillis(jwtUtils.getJwtExpiration()))
                    .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                    .username(loginRequest.getUsername())
                    .build();
        }
        return null;
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest){
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtUtils.generateTokenWithUsername(refreshTokenRequest.getUsername());
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtUtils.getJwtExpiration()))
                .username(refreshTokenRequest.getUsername())
                .build();
    }


    @Transactional(readOnly = true)
    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Usu??rio n??o encontrado: " + principal.getUsername()));
    }
}
