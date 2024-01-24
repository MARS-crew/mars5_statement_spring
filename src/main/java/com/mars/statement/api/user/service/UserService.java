package com.mars.statement.api.user.service;


import com.mars.statement.api.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
// @RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /*public void join(JoinDto joinDto){
        String username = joinDto.getUsername();
        String password = joinDto.getPassword();
        System.out.println(username);
        System.out.println(password);
        // 중복체크
        Boolean isExist = userRepository.existsByUsername(username);
        System.out.println(isExist);
        if(isExist){
            throw new RuntimeException(username + "는 이미 존재합니다.");
        }

        // 저장
        User user = User.builder()
                .username(username)
                .password(encoder.encode(password))
                .role("ROLE_ADMIN")
                .build();

        userRepository.save(user);
    }*/
}
