package com.mars.statement.api.auth.service;


import com.mars.statement.api.auth.domain.User;
import com.mars.statement.api.auth.dto.*;
import com.mars.statement.api.auth.repository.UserRepository;
import com.mars.statement.global.dto.CommonResponse;
import com.mars.statement.global.dto.TokenDto;
import com.mars.statement.global.exception.NotFoundException;
import com.mars.statement.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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

    @Autowired
    public AuthService(@Lazy JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> login(LoginRequest loginRequest) throws NotFoundException {

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
            user.updateName(loginRequest.getName());
        }
        // 구글 프로필 이미지 변경 시 업데이트
        if(!Objects.equals(user.getImg(), loginRequest.getPicture())){
            user.updateImg(loginRequest.getPicture());
        }
        // fcm token 변경 시 업데이트
        if(!Objects.equals(user.getFcmToken(), loginRequest.getFcmToken())){
            user.updateFcmToken(loginRequest.getFcmToken());
        }

        // 토큰 생성
        TokenDto tokenDto = jwtTokenProvider.issueToken(user.getId());

        user.updateRefreshToken(tokenDto.getRefreshToken());

        userRepository.save(user);

        // 리턴 값 변경 -> 토큰으로
        LoginResponse loginResponse = LoginResponse.builder()
                .id(user.getId())
                .accessToken(tokenDto.getAccessToken())
                .refreshToken(tokenDto.getRefreshToken())
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

    public ResponseEntity<?> reissueToken(TokenReissueRequest request){
        if(jwtTokenProvider.validateToken(request.getRefreshToken())){
            String id = jwtTokenProvider.getUserId(request.getRefreshToken());

            TokenDto tokenDto = jwtTokenProvider.reissueToken(Long.valueOf(id), request.getRefreshToken());

            return CommonResponse.createResponse(HttpStatus.OK.value(), "토큰 재발급 성공", tokenDto);
        }
        return CommonResponse.createResponseMessage(HttpStatus.BAD_REQUEST.value(), "refresh token이 만료되었습니다.");
    }
}
