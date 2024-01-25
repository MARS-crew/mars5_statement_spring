package com.mars.statement.api.chapter.service;

import com.mars.statement.api.chapter.domain.Chapter;
import com.mars.statement.api.chapter.domain.ChapterMember;
import com.mars.statement.api.chapter.dto.ChapterWithMemberDTO;
import com.mars.statement.api.chapter.repository.ChapterRepository;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChapterService {

    private final ChapterRepository chapterRepository;

    private final ModelMapper modelMapper;


    @Autowired
    public ChapterService(ChapterRepository chapterRepository, ModelMapper modelMapper){
        this.chapterRepository = chapterRepository;
        this.modelMapper = modelMapper;
    }
//
//    /*
//     * chapter id를 통해서 정보 가져오기
//     * @param id
//     * @return chapter 엔티티 또는 null
//     */


    public ChapterWithMemberDTO getChapterWithMembers(Long chapterId){

        Chapter chapter = chapterRepository.findChapterWithMembers(chapterId);

        return modelMapper.map(chapter, ChapterWithMemberDTO.class);
    }

//    public ChapterWithMemberDTO convertToDTO(Chapter chapter){
//        ChapterWithMemberDTO chapterDTO = new ChapterWithMemberDTO();
//
//        chapterDTO.setChapterId(chapter.getId());
//        chapterDTO.setSuggest(chapterDTO.getSuggest());
//        chapterDTO.
//
//    }
}
