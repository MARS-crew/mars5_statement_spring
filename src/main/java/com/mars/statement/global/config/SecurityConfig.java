package com.mars.statement.global.config;

import com.mars.statement.global.filter.JwtAuthorizationFilter;
import com.mars.statement.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.RequestCacheAwareFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                // cors 설정
                //.cors((cors) -> cors.configurationSource(apiConfigurationSource()))
                // csrf 비활성화(jwt방식은 세션 공격 상관 X)
                .csrf((auth)->auth.disable())
                // 세션 설정
                .sessionManagement((session)->session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // form로그인 방식 비활성
                .formLogin((auth) -> auth.disable())
                // http basic 인증 방식 비활성화
                .httpBasic((auth) -> auth.disable())
                // 인가 작업
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/api/v1/auth/login", "/error", "/favicon.ico").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**").permitAll()
                        .anyRequest().authenticated())
                // 커스텀 필터 등록
                .addFilterBefore(new JwtAuthorizationFilter(jwtTokenProvider), RequestCacheAwareFilter.class)
        // 권한 문제
        ;


        return http.build();
    }
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true)
                .setMatchingStrategy(MatchingStrategies.STRICT);

        return modelMapper;
    }
}
