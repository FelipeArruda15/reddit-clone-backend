package com.felipearruda.redditclone.controller;

import com.felipearruda.redditclone.dto.RefreshTokenRequest;
import com.felipearruda.redditclone.dto.RegisterRequest;
import com.felipearruda.redditclone.dto.AuthenticationResponse;
import com.felipearruda.redditclone.dto.LoginRequest;
import com.felipearruda.redditclone.service.AuthService;
import com.felipearruda.redditclone.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "API para os serviços de autenticação")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signup")
    @Operation(summary = "Inscrever-se", description = "Cadastra um novo usuário")
    public ResponseEntity signup(@RequestBody RegisterRequest registerRequest) {
        authService.signup(registerRequest);
        return new ResponseEntity<>("Usuário registrado com sucesso", HttpStatus.CREATED);
    }

    @GetMapping("/accountVerification/{token}")
    @Operation(summary = "Ativar conta", description = "Ativa a conta de um usuário, atráves do token passado como parâmetro",
        parameters = {@Parameter(in = ParameterIn.PATH, name = "token", description = "Token recebido por e-mail")}
    )
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
        authService.verifyAccount(token);
        return new ResponseEntity<>("Conta ativada com sucesso", HttpStatus.OK);
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);
    }

    @PostMapping("/refresh/token")
    @Operation(summary = "Refresh Token", description = "Atualiza token de acesso")
    public AuthenticationResponse refreshTokens(@Valid @RequestBody RefreshTokenRequest tokenRequest) {
        return authService.refreshToken(tokenRequest);
    }

    @PostMapping("/logout")
    @Operation(summary = "Sair", description = "Realizar o logout do app")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return new ResponseEntity<>("Refresh token excluído com sucesso!", HttpStatus.OK);
    }
}
