package com.example.syncrowbackend.user.controller;

import com.example.syncrowbackend.global.jwt.JwtProvider;
import com.example.syncrowbackend.global.jwt.RefreshTokenDto;
import com.example.syncrowbackend.global.jwt.TokenResponseDto;
import com.example.syncrowbackend.global.resolver.UserInfo;
import com.example.syncrowbackend.user.dto.LoginRequestDto;
import com.example.syncrowbackend.user.dto.LoginResponseDto;
import com.example.syncrowbackend.user.entity.User;
import com.example.syncrowbackend.user.service.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping
    public ResponseEntity<LoginResponseDto> login(@RequestParam String code) throws JsonProcessingException {
        LoginResponseDto responseDto = authService.login(code);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponseDto> refresh(@RequestBody @Valid RefreshTokenDto requestDto) {
        TokenResponseDto tokenResponseDto = authService.reissue(requestDto.getRefreshToken());
        return ResponseEntity.ok(tokenResponseDto);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(JwtProvider.AUTHORIZATION_HEADER) String bearerToken, @UserInfo User user) {
        authService.logout(bearerToken, user);
        return ResponseEntity.ok().build();
    }
}
