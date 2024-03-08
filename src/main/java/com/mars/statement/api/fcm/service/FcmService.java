package com.mars.statement.api.fcm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.net.HttpHeaders;
import com.mars.statement.api.fcm.dto.FcmDto;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;

@Service
@Slf4j
public class FcmService {

    @Value("${fcm.url}")
    private String FIREBASE_ALARM_SEND_API_URI;
    private final ObjectMapper objectMapper;

    @Autowired
    public FcmService(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    private String getAccessToken() throws IOException {
        // firebase로 부터 access token을 가져온다.

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource("firebase-secret.json").getInputStream())
                .createScoped(Arrays.asList("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();

        return googleCredentials.getAccessToken().getTokenValue();

    }

    /**
     * makeMessage : 알림 파라미터들을 FCM이 요구하는 body 형태로 가공한다.
     * @param targetToken : firebase token
     * @param title : 알림 제목
     * @param body : 알림 내용
     * @return
     * */
    public String makeMessage( String targetToken, String title, String body) throws JsonProcessingException {
        FcmDto fcmMessage = FcmDto.builder()
                .message(
                        FcmDto.Message.builder()
                                .token(targetToken)
                                .notification(
                                        FcmDto.Notification.builder()
                                                .title(title)
                                                .body(body)
                                                .build()
                                )
                                .build()
                )
                .validateOnly(false)
                .build();

        return objectMapper.writeValueAsString(fcmMessage);

    }

    /**
     * 알림 푸쉬를 보내는 역할을 하는 메서드
     * @param targetToken : 푸쉬 알림을 받을 클라이언트 앱의 식별 토큰
     * */
    public void sendMessageTo(String targetToken, String title, String body) throws IOException{

        String message = makeMessage(targetToken, title, body);

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(FIREBASE_ALARM_SEND_API_URI)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer "+getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        Response response = client.newCall(request).execute();

        log.info(response.body().string());
    }
}