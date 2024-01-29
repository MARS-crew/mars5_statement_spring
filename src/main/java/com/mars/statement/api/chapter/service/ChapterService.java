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
            return new ChapterWithMemberDTO(chapter.getId(),chapter.getSuggest().getSuggest(), chapter.getSuggest().getType(), chapterMemberDTOList);
        }

        return null;
    }

}
