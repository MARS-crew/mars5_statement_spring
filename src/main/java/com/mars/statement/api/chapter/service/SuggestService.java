package com.mars.statement.api.chapter.service;

import com.mars.statement.api.chapter.domain.Chapter;
import com.mars.statement.api.chapter.domain.Suggest;
import com.mars.statement.api.chapter.dto.ChapterMemberDTO;
import com.mars.statement.api.chapter.dto.ChapterWithMemberDTO;
import com.mars.statement.api.chapter.repository.ChapterRepository;
import com.mars.statement.api.chapter.repository.SuggestRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuggestService {

    private final SuggestRepository suggestRepository;



    @Autowired
    public SuggestService(SuggestRepository suggestRepository) {
        this.suggestRepository = suggestRepository;
    }

    public List<Suggest> getSuggestByGroupId(Long group_id){ return suggestRepository.findByGroupId(group_id); }



}
