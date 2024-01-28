package com.mars.statement.api.user.service;

import com.mars.statement.api.user.dto.GoogleLoginResponse;
import com.mars.statement.api.user.dto.GoogleRequestAccessTokenDto;
import com.mars.statement.api.user.dto.SocialAuthResponse;
import com.mars.statement.api.user.dto.SocialUserResponse;
import com.mars.statement.api.user.feign.GoogleAuthApi;
import com.mars.statement.api.user.feign.GoogleUserApi;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    // private final GoogleAuthApi googleAuthApi;
    private final GoogleUserApi googleUserApi;

    /*@Value("${social.client.google.id}")
    private String googleAppKey;
    @Value("${social.client.google.secret}")
    private String googleAppSecret;
    @Value("${social.client.google.redirect-uri}")
    private String googleRedirectUri;
    @Value("${social.client.google.grant-type}")
    private String googleGrantType;

    public SocialAuthResponse getAccessToken(String authorizationCode) {
        String decode = URLDecoder.decode(authorizationCode, StandardCharsets.UTF_8);
        ResponseEntity<?> response = googleAuthApi.getAccessToken(
                GoogleRequestAccessTokenDto.builder()
                        .code(decode)
                        .clientId(googleAppKey)
                        .clientSecret(googleAppSecret)
                        .redirectUri(googleRedirectUri)
                        .grantType(googleGrantType)
                        .build()
        );

        log.info("google auth info : " + response.toString());

        return new Gson().fromJson(
                response.getBody().toString(),
                SocialAuthResponse.class
        );
    }*/

    public SocialUserResponse getUserInfo(String accessToken) {
        ResponseEntity<?> response = googleUserApi.getUserInfo("Bearer "+accessToken);

        log.info("google user info : " + response.toString());
        String jsonString = response.getBody().toString();

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        GoogleLoginResponse googleLoginResponse = gson.fromJson(jsonString, GoogleLoginResponse.class);

        return SocialUserResponse.builder()
                .id(googleLoginResponse.getId())
                .email(googleLoginResponse.getEmail())
                .name(googleLoginResponse.getName())
                .picture(googleLoginResponse.getPicture())
                .build();
    }
}
