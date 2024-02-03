package com.mars.statement.api.share.repository;

import com.mars.statement.api.share.domain.Share;
import com.mars.statement.api.share.dto.ChapterShareDto;
import com.mars.statement.api.share.dto.PersonalShareDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShareRepository extends JpaRepository<Share, Long> {

    @Query("SELECT NEW com.mars.statement.api.share.dto.ShareDTO(" +
            "s.id as suggestId, s.suggest, " +
            "NEW com.mars.statement.api.share.dto.MemberOpinionDTO(" +
            "gm.id as memberId, gm.user.name as memberName, gm.user.img as memberImg, " +
            "NEW com.mars.statement.api.share.dto.OpinionDTO(" +
            "ch.id as chapterId, c.reg_dt as regDt, o.opinion, o.location" +
            ")" +
            ")" +
            ")" +
            "FROM ChapterMember c " +
            "JOIN c.chapter ch " +
            "JOIN ch.suggest s " +
            "JOIN c.groupMember gm " +
            "JOIN Share o ON o.chapterMember.id = c.id " +
            "WHERE ch.id IN :chapterIds")
    List<PersonalShareDto> findPersonalSharesByIds(@Param("chapterIds") List<Long> chapterIds);

    @Query("SELECT NEW com.mars.statement.api.share.dto.ChapterShareDto(" +
            "s.id as suggestId, s.suggest, " +
            "NEW com.mars.statement.api.share.dto.ChapterSummaryDto(" +
            "ch.id as chapterId, c.reg_dt as regDt, gm.user.name as memberName, c.summary, c.summary" +
            ")" +
            ")" +
            ")" +
            "FROM ChapterMember c " +
            "JOIN c.chapter ch " +
            "JOIN c.groupMember gm " +
            "WHERE ch.id IN :chapterIds")
    List<ChapterShareDto> findChapterSharesByIds(@Param("chapterIds") List<Long> chapterIds);
}
