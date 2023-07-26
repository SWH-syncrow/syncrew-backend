package com.example.syncrowbackend.user.service;

import com.example.syncrowbackend.common.exception.CustomException;
import com.example.syncrowbackend.common.exception.ErrorCode;
import com.example.syncrowbackend.common.jwt.TokenProvider;
import com.example.syncrowbackend.common.jwt.TokenResponseDto;
import com.example.syncrowbackend.common.redis.RedisUtil;
import com.example.syncrowbackend.user.dto.*;
import com.example.syncrowbackend.user.entity.User;
import com.example.syncrowbackend.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final WebClient webClient;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final RedisUtil redisUtil;

    @Value("${kakao.client-id}")
    private String CLIENT_ID;

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

    @Override
    @Transactional
    public LoginResponseDto login(LoginRequestDto requestDto) {
        KakaoUserDto kakaoUser = getKakaoUser(requestDto.getAccessToken());
        if (kakaoUser == null) {
            throw new CustomException(ErrorCode.AUTHENTICATION_FAILED, "카카오 사용자 정보를 가져오는데 실패했습니다.");
        }
        Optional<User> user = userRepository.findByKakaoId(kakaoUser.getId());

        if (user.isEmpty()) {
            User newUser = kakaoUser.toEntity();
            userRepository.save(newUser);
            return LoginResponseDto.builder()
                    .user(UserResponseDto.toDto(newUser))
                    .token(tokenProvider.issueToken(newUser))
                    .isNewUser(Boolean.TRUE)
                    .build();
        }

        return LoginResponseDto.builder()
                .user(UserResponseDto.toDto(user.get()))
                .token(tokenProvider.issueToken(user.get()))
                .isNewUser(Boolean.FALSE)
                .build();
    }

    @Override
    public TokenResponseDto reissue(ReissueRequestDto requestDto) {
        String refreshToken = requestDto.getRefreshToken();
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new CustomException(ErrorCode.AUTHENTICATION_FAILED, "리프레시 토큰이 유효하지 않습니다.");
        }

        String kakaoId = tokenProvider.getClaims(refreshToken).getSubject();
        User user = findUser(kakaoId);
        if (!redisUtil.hasKey(kakaoId) || !redisUtil.get(kakaoId).equals(refreshToken)) {
            throw new CustomException(ErrorCode.AUTHENTICATION_FAILED, "해당 리프레시 토큰이 존재하지 않습니다.");
        }

        return tokenProvider.issueToken(user);
    }

    @Override
    public void logout(HttpServletRequest request, User user) {
        String accessToken = tokenProvider.resolveToken(request);
        if (!StringUtils.hasText(accessToken)) {
            throw new CustomException(ErrorCode.AUTHENTICATION_FAILED, "액세스 토큰이 존재하지 않습니다.");
        }
        redisUtil.delete(user.getKakaoId());
        redisUtil.setBlackList(accessToken, "accessToken");
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

    public UserResponseDto getUser(User user) {
        return UserResponseDto.toDto(user);
    }
}
