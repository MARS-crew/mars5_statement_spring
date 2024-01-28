package com.mars.statement.api.user.service;


import com.mars.statement.api.user.domain.User;
import com.mars.statement.api.user.dto.*;
import com.mars.statement.api.user.repository.UserRepository;
import com.mars.statement.global.dto.CommonResponse;
import com.mars.statement.global.enums.UserRole;
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
public class UserService {
    private final UserRepository userRepository;
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    public ResponseEntity<?> doSocialLogin(LoginRequest loginRequest) throws NotFoundException {
        // 프론트에서 code를 전달할때 사용
        //SocialAuthResponse socialAuthResponse = authService.getAccessToken(loginRequest.getCode());
        // 프론트에서 accessToken을 전달하면 여기
        SocialUserResponse socialUserResponse = authService.getUserInfo(loginRequest.getAccessToken());
        log.info("socialUserResponse {} ", socialUserResponse.toString());

        if(userRepository.findByEmail(socialUserResponse.getEmail()).isEmpty()){
            this.joinUser(
                    JoinDto.builder()
                            .email(socialUserResponse.getEmail())
                            .name(socialUserResponse.getName())
                            .picture(socialUserResponse.getPicture())
                            .build()
            );
        }
        User user = userRepository.findByEmail(socialUserResponse.getEmail())
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND.value(), "유저 정보를 찾을 수 없습니다."));

        // 구글 프로필 이미지 변경 시 업데이트
        if(Objects.equals(user.getImg(), socialUserResponse.getPicture())){
            user.updateImg(socialUserResponse.getPicture());
        }

        // 토큰 생성 로직 작성
        String accessToken = jwtTokenProvider.createAccessToken(user);
        String refreshToken = jwtTokenProvider.createRefreshToken();

        user.updateRefreshToken(refreshToken);

        userRepository.save(user);

        // 리턴 값 변경 -> 토큰으로
        LoginResponse loginResponse = LoginResponse.builder()
                .id(user.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .role(user.getRole())
                .build();
        return CommonResponse.createResponse(HttpStatus.OK.value(), "로그인 성공", loginResponse);
    }

    private JoinResponse joinUser(JoinDto joinDto){
        User user = userRepository.save(
                User.builder()
                        .email(joinDto.getEmail())
                        .img(joinDto.getPicture())
                        .name(joinDto.getName())
                        .role(UserRole.ROLE_USER)
                        .build()
        );

        return JoinResponse.builder()
                .id(user.getId())
                .build();
    }

}
