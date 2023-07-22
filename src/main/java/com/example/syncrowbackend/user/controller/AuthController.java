package com.example.syncrowbackend.user.controller;

import com.example.syncrowbackend.global.jwt.TokenDto;
import com.example.syncrowbackend.user.dto.LoginRequestDto;
import com.example.syncrowbackend.user.service.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody @Valid LoginRequestDto requestDto) throws JsonProcessingException {
        TokenDto tokenDto = authService.login(requestDto);
        return ResponseEntity.ok(tokenDto);
    }
}
