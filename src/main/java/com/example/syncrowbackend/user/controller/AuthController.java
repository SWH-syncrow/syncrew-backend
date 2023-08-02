package com.example.syncrowbackend.user.controller;

import com.example.syncrowbackend.common.config.DisableSwaggerSecurity;
import com.example.syncrowbackend.common.jwt.TokenResponseDto;
import com.example.syncrowbackend.common.security.UserDetailsImpl;
import com.example.syncrowbackend.user.dto.LoginRequestDto;
import com.example.syncrowbackend.user.dto.LoginResponseDto;
import com.example.syncrowbackend.user.dto.ReissueRequestDto;
import com.example.syncrowbackend.user.dto.UserResponseDto;
import com.example.syncrowbackend.user.service.AuthServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "1. Auth", description = "사용자 인증 기능")
public class AuthController {

    private final AuthServiceImpl authService;

    @GetMapping("/test")
    public ResponseEntity<String> testApi(@RequestParam String code) {
        String kakaoToken = authService.getKakaoToken(code);
        return ResponseEntity.ok(kakaoToken);
    }

    @DisableSwaggerSecurity
    @Operation(summary = "user login", description = "사용자 로그인")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto requestDto) {
        LoginResponseDto responseDto = authService.login(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DisableSwaggerSecurity
    @Operation(summary = "token reissue", description = "토큰 재발급")
    @PostMapping("/reissue")
    public ResponseEntity<TokenResponseDto> reissue(@RequestBody @Valid ReissueRequestDto requestDto) {
        TokenResponseDto responseDto = authService.reissue(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "user logout", description = "사용자 로그아웃")
    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        authService.logout(request, userDetails.getUser());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "get user", description = "사용자 정보 조회")
    @GetMapping("/user")
    public ResponseEntity<UserResponseDto> getUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserResponseDto responseDto = authService.getUser(userDetails.getUser());
        return ResponseEntity.ok(responseDto);
    }
}
