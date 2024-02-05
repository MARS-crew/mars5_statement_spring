package com.mars.statement.api.auth.service;

import com.mars.statement.api.auth.domain.User;
import com.mars.statement.api.auth.dto.JoinDto;
import com.mars.statement.api.auth.dto.LoginRequest;
import com.mars.statement.api.auth.dto.LoginResponse;
import com.mars.statement.api.auth.dto.TokenReissueRequest;
import com.mars.statement.api.auth.repository.UserRepository;
import com.mars.statement.api.group.domain.Group;
import com.mars.statement.api.group.domain.GroupMember;
import com.mars.statement.api.group.domain.Invitation;
import com.mars.statement.api.group.repository.GroupMemberRepository;
import com.mars.statement.api.group.repository.InvitationRepository;
import com.mars.statement.global.dto.CommonResponse;
import com.mars.statement.global.dto.TokenDto;
import com.mars.statement.global.exception.NotFoundException;
import com.mars.statement.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final InvitationRepository invitationRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public ResponseEntity<?> login(LoginRequest loginRequest) throws NotFoundException {

        if(userRepository.findByEmail(loginRequest.getEmail()).isEmpty()){
            User join = joinUser(
                    JoinDto.builder()
                            .uid(loginRequest.getUid())
                            .email(loginRequest.getEmail())
                            .name(loginRequest.getName())
                            .picture(loginRequest.getPicture())
                            .fcmToken(loginRequest.getFcmToken())
                            .build()
            );

            checkInvitation(join);
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

        Long lastGroupId = Optional.ofNullable(user.getGroup())
                .map(Group::getId)
                .orElse(0L);

        // 리턴 값 변경 -> 토큰으로
        LoginResponse loginResponse = LoginResponse.builder()
                .id(user.getId())
                .accessToken(tokenDto.getAccessToken())
                .refreshToken(tokenDto.getRefreshToken())
                .lastGroupId(lastGroupId)
                .build();
        return CommonResponse.createResponse(HttpStatus.OK.value(), "로그인 성공", loginResponse);
    }

    private User joinUser(JoinDto joinDto){

        return userRepository.save(
                User.builder()
                        .uid(joinDto.getUid())
                        .email(joinDto.getEmail())
                        .img(joinDto.getPicture())
                        .name(joinDto.getName())
                        .fcmToken(joinDto.getFcmToken())
                        .build()
        );
    }

    private void checkInvitation (User user){
        List<Invitation> invitationList = invitationRepository.findByEmail(user.getEmail());

        for (Invitation invitation:invitationList){
            groupMemberRepository.save(GroupMember.builder()
                    .group(invitation.getGroup())
                    .user(user)
                    .constructor(false)
                    .build());
            user.updateLastGroupId(invitation.getGroup());
            invitation.inviteAccept(true);
        }
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
