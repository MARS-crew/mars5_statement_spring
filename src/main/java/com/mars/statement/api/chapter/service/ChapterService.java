package com.mars.statement.api.chapter.service;

import com.mars.statement.api.chapter.domain.Chapter;
import com.mars.statement.api.chapter.domain.Suggest;
import com.mars.statement.api.chapter.dto.ChapterMemberDTO;
import com.mars.statement.api.chapter.dto.ChapterWithMemberDTO;
import com.mars.statement.api.chapter.repository.ChapterRepository;
import com.mars.statement.api.chapter.repository.SuggestRepository;
import com.mars.statement.api.group.domain.Group;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChapterService {

    private final ChapterRepository chapterRepository;
    private final ChapterMemberService chapterMemberService;
    private final SuggestRepository suggestRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ChapterService(ChapterRepository chapterRepository, ChapterMemberService chapterMemberService, SuggestRepository suggestRepository, ModelMapper modelMapper) {
        this.chapterRepository = chapterRepository;
        this.chapterMemberService = chapterMemberService;
        this.suggestRepository = suggestRepository;
        this.modelMapper = modelMapper;
    }

    public Chapter findChapterById(Long id){
        return chapterRepository.findById(id).orElse(null);
    }

    public ChapterWithMemberDTO getChapterWithMembers(Long chapter_id) {

        Chapter chapter = chapterRepository.findChapterWithMembers(chapter_id);

        if(chapter != null) {
            List<ChapterMemberDTO> chapterMemberDTOList = chapter.getChapterMembers()
                    .stream()
                    .map(chapterMember -> new ChapterMemberDTO(
                            chapterMember.getGroupMember().getId(),
                            chapterMember.getSummary(),
                            chapterMember.getGroupMember().getUser().getName()
                    )).toList();
            return new ChapterWithMemberDTO(chapter.getId(), chapter.getSuggest().getSuggest(),chapter.getSuggest().getType(), chapterMemberDTOList);
        }

        return null;
    }
/*    @Transactional
    public Long createChapterAndAddMembers(Long suggestId, Long creatorId) {
        // 1회차 생성
        Suggest suggest = suggestRepository.findById(suggestId)
                .orElseThrow(() -> new RuntimeException("Suggest not found"));

        Chapter chapter = Chapter.builder()
                .suggest(suggest)
                .build();
        Chapter savedChapter = chapterRepository.save(chapter);

        // 1회차에 생성자 추가
        chapterMemberService.addMemberToChapter(savedChapter.getId(), creatorId);

        return savedChapter.getId();
    }*/
@Transactional
public Long createChapterAndAddMembers(Long suggestId, Long creatorId) {
    // 1. 주제 조회
    Suggest suggest = suggestRepository.findById(suggestId)
            .orElseThrow(() -> new RuntimeException("Suggest not found"));

    // 2. 그룹 조회 (주제의 그룹)
    Group group = suggest.getGroup();

    // 3. 회차 생성 및 그룹 설정
    Chapter chapter = Chapter.builder()
            .suggest(suggest)
            .group(group)  // 그룹 설정 추가
            .build();
    Chapter savedChapter = chapterRepository.save(chapter);

    // 4. 회차에 생성자 추가
    chapterMemberService.addMemberToChapter(savedChapter.getId(), creatorId);

    return savedChapter.getId();
}


}
