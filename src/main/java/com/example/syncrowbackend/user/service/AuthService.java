package com.example.syncrowbackend.user.service;

import com.example.syncrowbackend.common.jwt.TokenDto;
import com.example.syncrowbackend.common.jwt.TokenProvider;
import com.example.syncrowbackend.user.dto.KakaoUserDto;
import com.example.syncrowbackend.user.dto.LoginRequestDto;
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
    private final TokenProvider tokenProvider;
    private final RestTemplate restTemplate;

    public TokenDto login(LoginRequestDto requestDto) throws JsonProcessingException {
        KakaoUserDto kakaoUserDto = getKakaoUser(requestDto.getAccessToken());
        User user = registerIfNeeded(kakaoUserDto);
        return tokenProvider.issueToken(user.getEmail(), user.getRole());
    }

    private KakaoUserDto getKakaoUser(String accessToken) throws JsonProcessingException {
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

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        String name = jsonNode.get("kakao_account").get("name").asText();
        String email = jsonNode.get("kakao_account").get("email").asText();

        return new KakaoUserDto(name, email);
    }

    private User registerIfNeeded(KakaoUserDto kakaoUserInfo) {
        String email = kakaoUserInfo.getEmail();
        return userRepository.findByEmail(email).orElse(
                userRepository.save(new User(kakaoUserInfo))
        );
    }
}
