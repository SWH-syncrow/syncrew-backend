package com.example.syncrowbackend.auth.jwt;

import com.example.syncrowbackend.auth.security.UserDetailsServiceImpl;
import com.example.syncrowbackend.auth.service.RedisTokenService;
import com.example.syncrowbackend.common.exception.ErrorCode;
import com.example.syncrowbackend.common.exception.ErrorResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    private final TokenProvider tokenProvider;
    private final RedisTokenService redisTokenService;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Requested URL : {}", request.getRequestURI());

        String token = tokenProvider.extractTokenFromRequest(request);
        if (token != null) {
            try {
                Claims claims = validateToken(token);
                setAuthentication(claims.getSubject());
            } catch (ExpiredJwtException e) {
                handleTokenError(response, "토큰이 만료되었습니다.");
                return;
            } catch (SecurityException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
                handleTokenError(response, "지원되지 않는 토큰입니다.");
                return;
            }
            if (redisTokenService.isTokenBlacklisted(token)) {
                handleTokenError(response, "블랙리스트에 등록된 토큰입니다. 다시 로그인해주세요.");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private Claims validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(tokenProvider.getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private void handleTokenError(HttpServletResponse response, String message) throws IOException {
        ErrorResponseDto responseDto = new ErrorResponseDto(ErrorCode.AUTHENTICATION_FAILED, message);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(responseDto));
    }

    private void setAuthentication(String kakaoId) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(kakaoId);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    private Authentication createAuthentication(String kakaoId) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(kakaoId);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
