package com.example.syncrowbackend.user.service;

import com.example.syncrowbackend.common.jwt.TokenResponseDto;
import com.example.syncrowbackend.user.dto.LoginRequestDto;
import com.example.syncrowbackend.user.dto.LoginResponseDto;
import com.example.syncrowbackend.user.dto.ReissueRequestDto;
import com.example.syncrowbackend.user.dto.UserResponseDto;
import com.example.syncrowbackend.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    LoginResponseDto login(LoginRequestDto requestDto);
    TokenResponseDto reissue(ReissueRequestDto requestDto);
    void logout(HttpServletRequest request, User user);
    UserResponseDto getUser(User user);
}
