package com.example.syncrowbackend.auth.jwt;

import com.example.syncrowbackend.common.exception.ErrorCode;
import com.example.syncrowbackend.common.exception.ErrorResponseDto;
import com.example.syncrowbackend.auth.security.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
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
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    private final TokenProvider tokenProvider;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Requested URL : {}", request.getRequestURI());
        String token = tokenProvider.resolveToken(request);
        if (StringUtils.hasText(token)) {
            if (!tokenProvider.validateToken(token)) {
                ErrorResponseDto responseDto = new ErrorResponseDto(ErrorCode.AUTHENTICATION_FAILED, "인증 토큰이 올바른지 확인해주세요.");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json; charset=UTF-8");
                response.getWriter().write(objectMapper.writeValueAsString(responseDto));
                return;
            }
            Claims claims = tokenProvider.getClaims(token);
            setAuthentication(claims.getSubject());
        }
        filterChain.doFilter(request,response);
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
