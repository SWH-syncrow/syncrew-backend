package com.example.syncrowbackend.auth.service;

import com.example.syncrowbackend.auth.jwt.TokenResponseDto;
import com.example.syncrowbackend.auth.dto.LoginRequestDto;
import com.example.syncrowbackend.auth.dto.LoginResponseDto;
import com.example.syncrowbackend.auth.dto.ReissueRequestDto;
import com.example.syncrowbackend.auth.dto.UserResponseDto;
import com.example.syncrowbackend.auth.entity.User;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    LoginResponseDto login(LoginRequestDto requestDto);
    TokenResponseDto reissue(ReissueRequestDto requestDto);
    void logout(HttpServletRequest request, User user);
    UserResponseDto getUser(User user);
}
