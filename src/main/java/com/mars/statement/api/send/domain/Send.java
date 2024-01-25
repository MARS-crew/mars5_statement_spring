package com.mars.statement.api.send.domain;

import com.mars.statement.api.chapter.domain.Chapter;
import com.mars.statement.api.chapter.domain.ChapterMember;
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
@Table(name = "tbl_send_message")
public class Send {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "chapter_id", nullable = false)
    private Chapter chapter;
    @ManyToOne
    @JoinColumn(name = "from_id", nullable = false)
    private ChapterMember from;
    @ManyToOne
    @JoinColumn(name = "to_id", nullable = false)
    private ChapterMember to;

    @Column(name = "message", nullable = false, columnDefinition = "text")
    private String message;

    @Column(name = "reg_dt")
    @CreatedDate
    private Timestamp reg_dt;
    @Column(name = "bookmark_yn")
    private Boolean bookmark = false;
    @Column(name = "location", length = 100)
    private String location;
}
