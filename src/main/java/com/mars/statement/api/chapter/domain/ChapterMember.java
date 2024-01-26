package com.mars.statement.api.chapter.domain;

import com.mars.statement.api.group.domain.GroupMember;
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
    private Timestamp regDate;
}
