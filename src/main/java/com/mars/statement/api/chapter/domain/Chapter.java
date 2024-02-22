package com.mars.statement.api.chapter.domain;

import com.mars.statement.api.group.domain.Group;
import com.mars.statement.api.group.domain.GroupMember;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@Table(name = "tbl_chapter")
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "suggest_id", nullable = false)
    private Suggest suggest;
    @CreatedDate
    @Column(name = "reg_dt")
    private Timestamp reg_dt;
    @Column(name = "join_cnt")
    private Integer joinCnt;
    @Column(name = "write_cnt")
    private Integer writeCnt;
    @Column(name = "summary_bool")
    private Boolean summaryBool;
    @Column(name = "member_cnt")
    private Integer memberCnt;

    @Column(name = "seq")
    private Long seq;

    @OneToMany(mappedBy = "chapter")
    private Set<ChapterMember> chapterMembers;

    public Set<ChapterMember> getChapterMembersWithUserName(){
        for(ChapterMember member : chapterMembers){
            member.setUserName(member.getGroupMember().getUser().getName());
        }
        return chapterMembers;
    }

    public void increaseJoinCnt() {
        this.joinCnt++;
    }
    public void increaseWriteCnt() {
        this.writeCnt++;
    }
    public void changeSummaryBool() {
        this.summaryBool = true;
    }
}
