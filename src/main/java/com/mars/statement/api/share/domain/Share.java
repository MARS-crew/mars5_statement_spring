package com.mars.statement.api.share.domain;

import com.mars.statement.api.chapter.domain.ChapterMember;
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
@Table(name = "tbl_share_opinion")
public class Share {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private ChapterMember chapterMember;
    @Column(name = "opinion", nullable = false, columnDefinition = "text")
    private String opinion;
    @Column(name = "reg_dt")
    @CreatedDate
    private Timestamp regDt;
    @Column(name = "location", length = 100)
    private String location;
}
