package com.mars.statement.global.service;

import com.mars.statement.api.user.domain.User;
import com.mars.statement.api.user.repository.UserRepository;
import com.mars.statement.global.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        User user = userRepository.findById(Long.valueOf(id)).orElseThrow(
                () -> new UsernameNotFoundException("Invalid authentication !")
        );

        return new CustomUserDetails(user);
    }
}
