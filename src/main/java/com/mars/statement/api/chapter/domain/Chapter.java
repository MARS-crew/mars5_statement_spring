package com.mars.statement.api.chapter.domain;

import com.mars.statement.api.group.domain.Group;
import com.mars.statement.api.group.domain.GroupMember;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Builder
@Entity
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
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

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
