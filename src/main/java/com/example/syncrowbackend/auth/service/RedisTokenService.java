package com.example.syncrowbackend.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisTokenService {
    private final RedisTemplate<String, String> tokenRepository;
    private final RedisTemplate<String, String> blacklist;
    private static final String TOKEN_PREFIX = "jwt:";
    private static final String BLACKLIST_PREFIX = "jwt:blacklist:";

    public void saveRefreshToken(String kakaoId, String refreshToken, long expiration) {
        String key = generateRefreshTokenKey(kakaoId);
        tokenRepository.opsForValue().set(key, refreshToken, expiration, TimeUnit.MILLISECONDS);
    }

    public void removeRefreshToken(String kakaoId) {
        String key = generateRefreshTokenKey(kakaoId);
        tokenRepository.delete(key);
    }

    public boolean hasValidRefreshToken(String kakaoId, String refreshToken) {
        String storedRefreshToken = tokenRepository.opsForValue().get(generateRefreshTokenKey(kakaoId));
        return refreshToken.equals(storedRefreshToken);
    }

    public void addToBlacklist(String accessToken, long expiration) {
        String key = generateBlacklistKey(accessToken);
        blacklist.opsForValue().set(key, "blacklisted", expiration, TimeUnit.MILLISECONDS);
    }

    public boolean isTokenBlacklisted(String accessToken) {
        String key = generateBlacklistKey(accessToken);
        return blacklist.hasKey(key);
    }

    private String generateRefreshTokenKey(String kakaoId) {
        return TOKEN_PREFIX + kakaoId;
    }

    private String generateBlacklistKey(String accessToken) {
        return BLACKLIST_PREFIX + accessToken;
    }
}