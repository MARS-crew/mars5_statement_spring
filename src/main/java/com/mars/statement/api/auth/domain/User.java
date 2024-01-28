package com.mars.statement.api.auth.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Getter
@Table(name = "tbl_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "uid", nullable = false)
    private String uid;
    @Column(name = "name", nullable = false, length = 20)
    private String name;
    @Column(name = "email", nullable = false, length = 100)
    private String email;
    @Column(name = "img")
    private String img;
    @Column(name = "refresh_token")
    private String refreshToken;
    @Column(name = "fcm_token")
    private String fcmToken;
    @CreatedDate
    @Column(name = "reg_dt")
    private Timestamp regDate;

    public void updateName(String name) {
        this.name = name;
    }
    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateImg(String img) {
        this.img = img;
    }
}