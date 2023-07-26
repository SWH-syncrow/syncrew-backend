package com.example.syncrowbackend.global.interceptor;

import com.example.syncrowbackend.global.error.AuthenticationException;
import com.example.syncrowbackend.global.error.ErrorCode;
import com.example.syncrowbackend.global.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = jwtProvider.resolveToken(request);
        if (!StringUtils.hasText(token)) {
            throw new AuthenticationException(ErrorCode.TOKEN_NOT_FOUND);
        }
        jwtProvider.validateToken(token);
        return true;
    }
}
