package com.mars.statement.api.auth.service;


import com.mars.statement.api.auth.domain.User;
import com.mars.statement.api.auth.dto.*;
import com.mars.statement.api.auth.repository.UserRepository;
import com.mars.statement.global.dto.CommonResponse;
import com.mars.statement.global.exception.NotFoundException;
import com.mars.statement.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public ResponseEntity<?> doSocialLogin(LoginRequest loginRequest) throws NotFoundException {

        if(userRepository.findByEmail(loginRequest.getEmail()).isEmpty()){
            this.joinUser(
                    JoinDto.builder()
                            .uid(loginRequest.getUid())
                            .email(loginRequest.getEmail())
                            .name(loginRequest.getName())
                            .picture(loginRequest.getPicture())
                            .fcmToken(loginRequest.getFcmToken())
                            .build()
            );
        }
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND.value(), "유저 정보를 찾을 수 없습니다."));

        // 구글 닉네임 변경 시 업데이트
        if(!Objects.equals(user.getName(), loginRequest.getName())){
            user.updateImg(loginRequest.getName());
        }
        // 구글 프로필 이미지 변경 시 업데이트
        if(!Objects.equals(user.getImg(), loginRequest.getPicture())){
            user.updateImg(loginRequest.getPicture());
        }

        // 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(user);
        String refreshToken = jwtTokenProvider.createRefreshToken();

        user.updateRefreshToken(refreshToken);

        userRepository.save(user);

        // 리턴 값 변경 -> 토큰으로
        LoginResponse loginResponse = LoginResponse.builder()
                .id(user.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        return CommonResponse.createResponse(HttpStatus.OK.value(), "로그인 성공", loginResponse);
    }

    private JoinResponse joinUser(JoinDto joinDto){
        User user = userRepository.save(
                User.builder()
                        .uid(joinDto.getUid())
                        .email(joinDto.getEmail())
                        .img(joinDto.getPicture())
                        .name(joinDto.getName())
                        .fcmToken(joinDto.getFcmToken())
                        .build()
        );

        return JoinResponse.builder()
                .id(user.getId())
                .build();
    }

}
