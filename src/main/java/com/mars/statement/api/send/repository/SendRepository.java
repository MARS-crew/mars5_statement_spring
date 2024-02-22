package com.mars.statement.api.send.repository;

import com.mars.statement.api.chapter.dto.CheckChapterDto;
import com.mars.statement.api.send.domain.Send;
import com.mars.statement.api.send.dto.PersonalSendDto;
import com.mars.statement.api.send.dto.SendDetailDto;
import com.mars.statement.api.share.dto.ShareDetailDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface SendRepository extends JpaRepository<Send, Long> {


    @Query("SELECT NEW com.mars.statement.api.send.dto.PersonalSendDto(" +
            "s.id AS suggestId, s.suggest, " +
            "NEW com.mars.statement.api.send.dto.MemberMessageDto(" +
            "gm.id AS memberId, u.name AS memberName, u.img AS memberImg, " +
            "NEW com.mars.statement.api.send.dto.MessageDto(" +
            "c.seq, c.id AS chapterId, cm.id AS chapterMemberId, c.reg_dt AS regDt, m.message, m.location" +
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
            "WHERE c.id IN :chapterIds AND my_cm.groupMember.id = :myId " +
            "ORDER BY c.seq DESC")
    List<PersonalSendDto> findPersonalSharesByIds(@Param("chapterIds") List<Long> chapterIds, @Param("myId") Long myId);

    @Query("SELECT NEW com.mars.statement.api.chapter.dto.CheckChapterDto(" +
            "s.id AS suggestId, s.suggest, " +
            "NEW com.mars.statement.api.chapter.dto.ChapterSummaryDto(" +
            "ch.seq, ch.id AS chapterId, c.reg_dt AS regDt, u.name AS memberName, c.summary" +
            ")" +
            ")" +
            "FROM ChapterMember c " +
            "JOIN c.chapter ch " +
            "JOIN ch.suggest s " +
            "JOIN c.groupMember gm " +
            "JOIN gm.user u " +
            "WHERE ch.id IN :chapterIds AND u.id = :myId " +
            "ORDER BY ch.seq DESC")
    List<CheckChapterDto> findChapterSendsByIds(@Param("chapterIds") List<Long> chapterIds, @Param("myId") Long myId);

    @Query("SELECT NEW com.mars.statement.api.send.dto.SendDetailDto(" +
            "s.id AS suggestId, s.suggest, " +
            "c.seq, my.id AS chapterId, c.reg_dt AS regDt, my.summary, " +
            "NEW com.mars.statement.api.send.dto.SendMemberDetailDto(" +
            "m.id AS sendId, m.from.id AS memberId, u.name AS memberName, " +
            "m.message, m.location, m.bookmark " +
            ")" +
            ") " +
            "FROM Chapter c " +
            "JOIN c.suggest s " +
            "JOIN c.chapterMembers my " +
            "JOIN c.chapterMembers cm " +
            "JOIN Send m ON m.chapter.id = c.id AND m.from.id = cm.id AND m.to.id = :myId " +
            "JOIN cm.groupMember gm " +
            "JOIN gm.user u " +
            "WHERE c.id = :chapterId and cm.id != :myId AND my.id = :myId")
    List<SendDetailDto> findSendDetails(@Param("chapterId")Long chapterId, @Param("myId") Long myId);

    @Transactional
    @Modifying( clearAutomatically=true)
    @Query("UPDATE Send s " +
            "SET s.bookmark = CASE WHEN s.bookmark = true THEN false ELSE true END " +
            "WHERE s.id = :sendId")
    int updateBookmark(@Param("sendId")Long sendId);

}
