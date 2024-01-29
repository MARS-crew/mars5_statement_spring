package com.mars.statement.api.group.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Timestamp;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "tbl_invitation_email")
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;
    @Column(name = "email", nullable = false, length = 100)
    private String email;
    @Column(name = "invite_dt")
    @CreatedDate
    private Timestamp inviteDate;
    @Column(name = "is_accept", nullable = false)
    private Boolean accepted;

    @Column(name = "accept_dt")
    private Timestamp acceptDate;
}
