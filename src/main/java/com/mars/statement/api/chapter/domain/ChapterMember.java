package com.mars.statement.api.chapter.domain;

import com.mars.statement.api.group.domain.GroupMember;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import java.util.Set;

@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "tbl_chapter_member")
public class ChapterMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chapter_id", nullable = false)
    private Chapter chapter;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private GroupMember groupMember;

    @Column(name = "summary", columnDefinition = "text")
    private String summary;

    @Column(name = "reg_dt")
    @CreatedDate
    private Timestamp reg_dt;

    @Column(name = "is_constructor")
    private Boolean constructor;

    @Transient
    private String userName;

}
