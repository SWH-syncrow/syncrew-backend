package com.example.syncrowbackend.user.controller;

import com.example.syncrowbackend.common.jwt.TokenResponseDto;
import com.example.syncrowbackend.common.security.UserDetailsImpl;
import com.example.syncrowbackend.user.dto.LoginRequestDto;
import com.example.syncrowbackend.user.dto.LoginResponseDto;
import com.example.syncrowbackend.user.dto.ReissueRequestDto;
import com.example.syncrowbackend.user.service.AuthServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthServiceImpl authService;

    @GetMapping("/test")
    public String testApi(@RequestParam String code) {
        return authService.getKakaoToken(code);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto requestDto) {
        LoginResponseDto responseDto = authService.login(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponseDto> refresh(@RequestBody @Valid ReissueRequestDto requestDto) {
        TokenResponseDto tokenResponseDto = authService.reissue(requestDto);
        return ResponseEntity.ok(tokenResponseDto);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        authService.logout(request, userDetails.getUser());
        return ResponseEntity.ok().build();
    }
}
