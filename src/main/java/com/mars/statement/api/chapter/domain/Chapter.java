package com.mars.statement.api.chapter.domain;

import com.mars.statement.api.group.domain.Group;
import com.mars.statement.api.group.domain.GroupMember;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "tbl_chapter")
public class Chapter {

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
    private Timestamp reg_dt;

    @Column(name = "del_dt")
    private Timestamp del_dt;

    @Column(name = "type", nullable = false, length = 20)
    private String type;

    @ManyToOne
    @JoinColumn(name = "constructor_id")
    private GroupMember groupMember;

    @OneToMany(mappedBy = "chapter")
    private Set<ChapterMember> chapterMembers;

    public Set<ChapterMember> getChapterMembersWithUserName(){
        for(ChapterMember member : chapterMembers){
            member.setUserName(member.getGroupMember().getUser().getName());
        }
        return chapterMembers;
    }
}
