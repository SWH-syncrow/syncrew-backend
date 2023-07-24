package com.example.syncrowbackend.user.service;

import com.example.syncrowbackend.global.error.AuthenticationException;
import com.example.syncrowbackend.global.error.BusinessException;
import com.example.syncrowbackend.global.error.ErrorCode;
import com.example.syncrowbackend.global.jwt.JwtProvider;
import com.example.syncrowbackend.global.jwt.TokenResponseDto;
import com.example.syncrowbackend.global.redis.RedisUtil;
import com.example.syncrowbackend.user.dto.KakaoUserDto;
import com.example.syncrowbackend.user.dto.LoginResponseDto;
import com.example.syncrowbackend.user.dto.UserResponseDto;
import com.example.syncrowbackend.user.entity.User;
import com.example.syncrowbackend.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final RedisUtil redisUtil;

    public LoginResponseDto login(String kakaoToken) throws JsonProcessingException {
        KakaoUserDto kakaoUserDto = getKakaoUserInfo(kakaoToken);
        User user = registerIfNeeded(kakaoUserDto);
        TokenResponseDto token = jwtProvider.issueToken(user);

        return new LoginResponseDto(UserResponseDto.of(user), token);
    }

    public TokenResponseDto reissue(String refreshToken) {
        jwtProvider.validateToken(refreshToken);

        String email = jwtProvider.getClaims(refreshToken).getSubject();
        User user = findUser(email);
        if (!redisUtil.hasKey(email) || !redisUtil.get(email).equals(refreshToken)) {
           throw new AuthenticationException(ErrorCode.TOKEN_NOT_FOUND);
        }

        return jwtProvider.issueToken(user);
    }

    public void logout(String bearerToken, User user) {
        String accessToken = bearerToken.substring(7);
        redisUtil.delete(user.getEmail());
        long expiration = jwtProvider.getExpiration(accessToken);
        redisUtil.setBlackList(accessToken, "accessToken", expiration);
    }

    private KakaoUserDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        URI uri = UriComponentsBuilder
                .fromUriString("https://kapi.kakao.com")
                .path("/v2/user/me")
                .encode()
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(new LinkedMultiValueMap<>());

        ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        String name = jsonNode.get("properties").get("nickname").asText();
        String email = jsonNode.get("kakao_account").get("email").asText();

        return new KakaoUserDto(name, email);
    }

    private User registerIfNeeded(KakaoUserDto kakaoUserInfo) {
        String email = kakaoUserInfo.getEmail();
        return userRepository.findByEmail(email).orElse(
                userRepository.save(new User(kakaoUserInfo))
        );
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }
}
