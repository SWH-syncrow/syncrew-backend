package com.example.syncrowbackend.auth.jwt;

import com.example.syncrowbackend.common.redis.RedisUtil;
import com.example.syncrowbackend.auth.entity.User;
import com.example.syncrowbackend.auth.entity.UserRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class TokenProvider {
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

    public TokenProvider(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secret);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public TokenResponseDto issueToken(User user) {
        String kakaoId = user.getKakaoId();
        UserRole role = user.getRole();
        String accessToken = createAccessToken(kakaoId, role);
        String refreshToken = createRefreshToken(kakaoId);

        redisUtil.set(kakaoId, refreshToken);

        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private String createAccessToken(String kakaoId, UserRole role) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(kakaoId)
                .claim(AUTHORIZATION_KEY, role)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    private String createRefreshToken(String kakaoId) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(kakaoId)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        return this.getClaims(token) != null && !redisUtil.isOnBlackList(token);
    }

    public long getExpiration(String token) {
        return new Date().getTime() - getClaims(token).getExpiration().getTime();
    }

    public Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SecurityException e) {
            log.error("잘못된 JWT 서명입니다.");
        } catch (MalformedJwtException e) {
            log.error("유효하지 않은 JWT 토큰입니다.");
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰이 잘못되었습니다.");
        }
        return null;
    }
}
