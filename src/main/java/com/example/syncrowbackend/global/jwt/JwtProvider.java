package com.example.syncrowbackend.global.jwt;

import com.example.syncrowbackend.global.error.AuthenticationException;
import com.example.syncrowbackend.global.error.ErrorCode;
import com.example.syncrowbackend.global.redis.RedisUtil;
import com.example.syncrowbackend.user.entity.User;
import com.example.syncrowbackend.user.entity.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtProvider {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_KEY = "auth";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final long ACCESS_TOKEN_EXPIRATION_TIME = 1000L * 60 * 30; // 30분
    public static final long REFRESH_TOKEN_EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 3; // 3일

    @Value("${jwt.secret}")
    private String secret;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    private final RedisUtil redisUtil;

    public JwtProvider(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secret);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public TokenResponseDto issueToken(User user) {
        String email = user.getEmail();
        UserRole role = user.getRole();
        String accessToken = createAccessToken(email, role);
        String refreshToken = createRefreshToken(email);

        if(redisUtil.hasKey(email)) {
            redisUtil.delete(email);
        }
        redisUtil.set(email, refreshToken, REFRESH_TOKEN_EXPIRATION_TIME);

        return new TokenResponseDto(accessToken, refreshToken);
    }

    private String createAccessToken(String email, UserRole role) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(email)
                .claim(AUTHORIZATION_KEY, role)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    private String createRefreshToken(String email) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    public String resolveToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (!StringUtils.hasText(authorizationHeader)) {
            throw new AuthenticationException(ErrorCode.EMPTY_AUTHORIZATION_HEADER);
        }
        if(!authorizationHeader.startsWith(BEARER_PREFIX)) {
            throw new AuthenticationException(ErrorCode.INVALID_TOKEN_TYPE);
        }
        return authorizationHeader.substring(7);
    }

    public void validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            if(redisUtil.isOnBlackList(token)) {
                throw new AuthenticationException(ErrorCode.BLACKLISTED_TOKEN);
            }
        } catch (ExpiredJwtException e) {
            throw new AuthenticationException(ErrorCode.EXPIRED_TOKEN);
        } catch (Exception e) {
            throw new AuthenticationException(ErrorCode.INVALID_TOKEN);
        }
    }

    public long getExpiration(String token) {
        return new Date().getTime() - getClaims(token).getExpiration().getTime();
    }

    public Claims getClaims(String token) {
        Claims claims;
        try {
            claims = Jwts.parserBuilder().setSigningKey(key).build()
                    .parseClaimsJws(token).getBody();
        } catch (Exception e) {
            throw new AuthenticationException(ErrorCode.INVALID_TOKEN);
        }
        return claims;
    }
}
