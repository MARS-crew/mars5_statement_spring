package com.mars.statement.api.user.service;


import com.mars.statement.api.user.domain.User;
import com.mars.statement.api.user.dto.JoinDto;
import com.mars.statement.api.user.dto.JoinResponse;
import com.mars.statement.api.user.dto.LoginRequest;
import com.mars.statement.api.user.repository.UserRepository;
import com.mars.statement.api.user.dto.LoginResponse;
import com.mars.statement.api.user.dto.SocialAuthResponse;
import com.mars.statement.api.user.dto.SocialUserResponse;
import com.mars.statement.global.enums.UserRole;
import com.mars.statement.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final AuthService authService;

    public LoginResponse doSocialLogin(LoginRequest loginRequest) throws NotFoundException {
        SocialAuthResponse socialAuthResponse = authService.getAccessToken(loginRequest.getCode());
        SocialUserResponse socialUserResponse = authService.getUserInfo(socialAuthResponse.getAccess_token());
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

        // 토큰 생성 로직 작성

        // 리턴 값 변경 -> 토큰으로
        return LoginResponse.builder()
                .id(user.getId())
                .build();
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
