package com.mars.statement.global.service;

import com.mars.statement.api.user.domain.User;
import com.mars.statement.api.user.repository.UserRepository;
import com.mars.statement.global.dto.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userData = userRepository.findByName(username);

        if (userData != null) {
            return new CustomUserDetails(userData);
        }

        return null;
    }
}