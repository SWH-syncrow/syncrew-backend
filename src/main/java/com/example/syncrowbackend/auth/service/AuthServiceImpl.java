package com.example.syncrowbackend.auth.service;

import com.example.syncrowbackend.auth.dto.KakaoUserDto;
import com.example.syncrowbackend.auth.dto.LoginRequestDto;
import com.example.syncrowbackend.auth.dto.LoginResponseDto;
import com.example.syncrowbackend.auth.dto.UserResponseDto;
import com.example.syncrowbackend.auth.entity.User;
import com.example.syncrowbackend.auth.jwt.TokenDto;
import com.example.syncrowbackend.auth.jwt.TokenProvider;
import com.example.syncrowbackend.auth.repository.UserRepository;
import com.example.syncrowbackend.common.exception.CustomException;
import com.example.syncrowbackend.common.exception.ErrorCode;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final WebClient webClient;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final RedisTokenService redisTokenService;

    @Value("${kakao.client-id}")
    private String CLIENT_ID;

    private static final int REFRESH_TOKEN_EXPIRATION_DAYS = 3;

    @Override
    @Transactional
    public LoginResponseDto login(LoginRequestDto requestDto, HttpServletResponse response) {
        KakaoUserDto kakaoUser = getKakaoUser(requestDto.getAccessToken());
        Optional<User> userOptional = userRepository.findByKakaoId(kakaoUser.getId());

        User user;
        boolean isNewUser;

        if (userOptional.isEmpty()) {
            user = userRepository.save(kakaoUser.toEntity());
            isNewUser = true;
        } else {
            user = userOptional.get();
            isNewUser = false;
        }

        return LoginResponseDto.builder()
                .user(new UserResponseDto(user, isNewUser || isTestTarget(user)))
                .token(handleTokenResponse(user, response))
                .isNewUser(isNewUser)
                .build();
    }

    @Override
    @Transactional
    public TokenDto reissue(String refreshToken, HttpServletResponse response) {
        String kakaoId = validateRefreshToken(refreshToken);
        return handleTokenResponse(findUser(kakaoId), response);
    }

    @Override
    @Transactional
    public void logout(HttpServletRequest request, User user) {
        String accessToken = tokenProvider.extractTokenFromRequest(request);

        redisTokenService.removeRefreshToken(user.getKakaoId());
        redisTokenService.addToBlacklist(accessToken, tokenProvider.getAtkExpirationTime());
    }

    @Override
    @Transactional
    public UserResponseDto getUser(User user) {
        return new UserResponseDto(user, isTestTarget(user));
    }

    private String validateRefreshToken(String refreshToken) {
        if (refreshToken == null) {
            throw new CustomException(ErrorCode.AUTHENTICATION_FAILED, "쿠키에 리프레시 토큰이 존재하지 않습니다.");
        }
        String kakaoId = tokenProvider.getClaims(refreshToken).getSubject();
        if (!redisTokenService.hasValidRefreshToken(kakaoId, refreshToken)) {
            throw new CustomException(ErrorCode.AUTHENTICATION_FAILED, "쿠키에 저장된 리프레시 토큰이 유효하지 않습니다.");
        }
        return kakaoId;
    }

    private TokenDto handleTokenResponse(User user, HttpServletResponse response) {
        TokenDto tokens = tokenProvider.issueToken(user);
        redisTokenService.saveRefreshToken(user.getKakaoId(), tokens.getRefreshToken(), tokenProvider.getRtkExpirationTime());
        setTokensInResponse(tokens, response);
        return tokens;
    }

    private void setTokensInResponse(TokenDto tokens, HttpServletResponse response) {
        response.setHeader("Authorization", "Bearer " + tokens.getAccessToken());
        addRefreshTokenCookie(tokens.getRefreshToken(), response);
    }

    private void addRefreshTokenCookie(String refreshToken, HttpServletResponse response) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setMaxAge((int) TimeUnit.DAYS.toSeconds(REFRESH_TOKEN_EXPIRATION_DAYS));
        refreshTokenCookie.setPath("/");
        response.addCookie(refreshTokenCookie);
    }

    public String getKakaoToken(String code) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", CLIENT_ID);
        body.add("redirect_uri", "http://localhost:8080/api/auth/test");
        body.add("code", code);

        return webClient.post()
                .uri("https://kauth.kakao.com/oauth/token")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    private KakaoUserDto getKakaoUser(String accessToken) {
        return webClient.get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(new CustomException(ErrorCode.AUTHENTICATION_FAILED, "액세스 토큰이 올바르지 않습니다.")))
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new CustomException(ErrorCode.AUTHENTICATION_FAILED, "내부 서버 오류가 발생했습니다.")))
                .bodyToMono(KakaoUserDto.class)
                .block();
    }

    private User findUser(String kakaoId) {
        return userRepository.findByKakaoId(kakaoId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND_ERROR, "해당 사용자가 존재하지 않습니다."));
    }

    private boolean isTestTarget(User user) {
        return user.getUserGroups().size() == 0;
    }
}
