package com.mars.statement.domain.user.service;


import com.mars.statement.domain.user.User;
import com.mars.statement.domain.user.dto.JoinDTO;
import com.mars.statement.domain.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
// @RequiredArgsConstructor <- 이것도 생성자 처리되는 Lombok의 어노테이션 간단하긴한데 순서문제가 생긴다함
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public void join(JoinDTO joinDTO){
        String username = joinDTO.getUsername();
        String password = joinDTO.getPassword();
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
    }
}
