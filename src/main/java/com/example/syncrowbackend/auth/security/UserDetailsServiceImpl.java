package com.example.syncrowbackend.auth.security;

import com.example.syncrowbackend.auth.entity.User;
import com.example.syncrowbackend.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String kakaoId) throws UsernameNotFoundException {
        User user = userRepository.findByKakaoId(kakaoId)
                .orElseThrow(() -> new UsernameNotFoundException("해당 kakao id를 가진 사용자가 존재하지 않습니다."));

        return new UserDetailsImpl(user);
    }
}
