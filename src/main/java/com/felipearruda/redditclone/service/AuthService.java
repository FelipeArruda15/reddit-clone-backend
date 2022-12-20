package com.felipearruda.redditclone.service;

import com.felipearruda.redditclone.dto.RegisterRequest;
import com.felipearruda.redditclone.exception.SpringRedditException;
import com.felipearruda.redditclone.model.NotificationEmail;
import com.felipearruda.redditclone.model.User;
import com.felipearruda.redditclone.model.VerificationToken;
import com.felipearruda.redditclone.repository.UserRepository;
import com.felipearruda.redditclone.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final EmailContentBuilder emailContentBuilder;
    private final EmailService emailService;

    @Transactional
    public void signup(RegisterRequest registerRequest) {
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getUsername()));
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

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new SpringRedditException("Token inválido"));
        fetchUserAndEnable(verificationToken.get());
    }

    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken){
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new SpringRedditException("Usuário não encontrado"));
        user.setActivated(true);
        userRepository.save(user);
    }
}
