package com.mars.statement.api.user.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Timestamp;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "tbl_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false, length = 20)
    private String name;
    @Column(name = "email", nullable = false, length = 100)
    private String email;
    @Column(name = "img")
    private String img;
    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;
    @CreatedDate
    @Column(name = "reg_dt")
    private Timestamp reg_dt;
    @Column(name = "del_dt")
    private Timestamp del_dt;
    @Column(name = "role", nullable = false, length = 20)
    private String role = "ROLE_USER";
}