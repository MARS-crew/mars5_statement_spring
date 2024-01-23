package com.mars.statement.api.share.domain;

import com.mars.statement.api.chapter.domain.ChapterMember;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "tbl_opinion_like")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "share_id", nullable = false)
    private Share share;
    @ManyToOne
    @JoinColumn(name = "writer_id", nullable = false)
    private ChapterMember writer;
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private ChapterMember member;

    @Column(name = "like_yn")
    private Boolean like = false;
}
