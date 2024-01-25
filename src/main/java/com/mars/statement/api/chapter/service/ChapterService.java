package com.mars.statement.api.chapter.service;

import com.mars.statement.api.chapter.domain.Chapter;
import com.mars.statement.api.chapter.domain.ChapterMember;
import com.mars.statement.api.chapter.dto.ChapterMemberDTO;
import com.mars.statement.api.chapter.dto.ChapterWithMemberDTO;
import com.mars.statement.api.chapter.repository.ChapterRepository;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChapterService {

    private final ChapterRepository chapterRepository;

    private final ModelMapper modelMapper;


    @Autowired
    public ChapterService(ChapterRepository chapterRepository, ModelMapper modelMapper) {
        this.chapterRepository = chapterRepository;
        this.modelMapper = modelMapper;
    }
//
//    /*
//     * chapter id를 통해서 정보 가져오기
//     * @param id
//     * @return chapterDTO
//     */
    public ChapterWithMemberDTO getChapterWithMembers(Long chapterId) {

        ChapterWithMemberDTO chapterDTO = new ChapterWithMemberDTO();

        Chapter chapter = chapterRepository.findChapterWithMembers(chapterId);

        chapterDTO.setChapterId(chapter.getId());
        chapterDTO.setSuggest(chapter.getSuggest());
        chapterDTO.setType(chapter.getType());

        List<ChapterMemberDTO> chapterMemberDTOList = chapter.getChapterMembers()
                .stream()
                .map(chapterMember -> new ChapterMemberDTO(
                        chapterMember.getId(),
                        chapterMember.getSummary(),
                        chapterMember.getGroupMember().getUser().getName()
                )).toList();
        chapterDTO.setChapterMembers(chapterMemberDTOList);

        return chapterDTO;

    }

}
