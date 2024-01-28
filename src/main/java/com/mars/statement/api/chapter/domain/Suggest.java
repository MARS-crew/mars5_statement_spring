package com.mars.statement.api.chapter.domain;

import com.mars.statement.api.group.domain.Group;
import com.mars.statement.api.group.domain.GroupMember;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "tbl_suggest")
public class Suggest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;
    @Column(name = "suggest", length = 100)
    private String suggest;
    @CreatedDate
    @Column(name = "reg_dt")
    private Timestamp regDate;
    @Column(name = "type", nullable = false, length = 20)
    private String type;
    @ManyToOne
    @JoinColumn(name = "constructor_id")
    private GroupMember groupMember;
}

