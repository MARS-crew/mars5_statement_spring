package com.mars.statement.api.send.domain;

import com.mars.statement.api.chapter.domain.Chapter;
import com.mars.statement.api.chapter.domain.ChapterMember;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "tbl_send_message",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_send_combination",
                        columnNames = {"chapter_id", "from_id", "to_id"}
                )
        }
)
@EntityListeners(AuditingEntityListener.class)
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
    private Boolean bookmark;

    @Column(name = "location", length = 100)
    private String location;
}
