package com.mars.statement.api.chapter.domain;

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


    @OneToMany(mappedBy = "chapter")
    private Set<ChapterMember> chapterMembers;

    public Set<ChapterMember> getChapterMembersWithUserName(){
        for(ChapterMember member : chapterMembers){
            member.setUserName(member.getGroupMember().getUser().getName());
        }
        return chapterMembers;
    }

}
