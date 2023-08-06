package com.example.syncrowbackend.auth.service;

import com.example.syncrowbackend.auth.jwt.TokenDto;
import com.example.syncrowbackend.auth.dto.LoginRequestDto;
import com.example.syncrowbackend.auth.dto.LoginResponseDto;
import com.example.syncrowbackend.auth.dto.UserResponseDto;
import com.example.syncrowbackend.auth.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    LoginResponseDto login(LoginRequestDto requestDto, HttpServletResponse response);
    TokenDto reissue(String requestToken, HttpServletResponse response);
    void logout(HttpServletRequest request, User user);
    UserResponseDto getUser(User user);
}
