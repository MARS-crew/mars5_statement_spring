package com.mars.statement.api.send.repository;

import com.mars.statement.api.send.domain.Send;
import com.mars.statement.api.send.dto.PersonalSendDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SendRepository extends JpaRepository<Send, Long> {


    @Query("SELECT NEW com.mars.statement.api.send.dto.PersonalSendDto(" +
            "s.id as suggestId, s.suggest, " +
            "NEW com.mars.statement.api.send.dto.MemberMessageDto(" +
            "gm.id as memberId, u.name as memberName, u.img as memberImg, " +
            "NEW com.mars.statement.api.send.dto.MessageDto(" +
            "c.id as chapterId, cm.id as chapterMemberId, c.reg_dt as regDt, m.message, m.location" +
            ")" +
            ")" +
            ")" +
            "FROM Chapter c " +
            "INNER JOIN c.suggest s " +
            "INNER JOIN c.chapterMembers my_cm " +
            "LEFT JOIN Send m ON m.to.id = my_cm.id " +
            "LEFT JOIN ChapterMember cm ON cm.id = m.from.id " +
            "INNER JOIN GroupMember gm ON gm.id = cm.groupMember.id " +
            "INNER JOIN User u ON u.id = gm.user.id " +
            "WHERE c.id IN :chapterIds AND my_cm.groupMember.id = :my_id")
    List<PersonalSendDto> findPersonalSharesByIds(@Param("chapterIds") List<Long> chapterIds, @Param("my_id") Long my_id);


}
