package com.mars.statement.global.dto;

import com.mars.statement.api.user.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {
    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    // Role 값 반환 (복잡함
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });

        return collection;
    }

    // Password 반환
    @Override
    public String getPassword() { return null; }

    // ID 반환
    @Override
    public String getUsername() {
        return user.getName();
    }

    // 계정이 만료 됐는지
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정이 차단됐는지
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 자격증명 만료 파악
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
