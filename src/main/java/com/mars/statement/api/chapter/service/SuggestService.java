package com.mars.statement.api.chapter.service;

import com.mars.statement.api.chapter.domain.Suggest;
import com.mars.statement.api.chapter.repository.SuggestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SuggestService {

    private final SuggestRepository suggestRepository;



    @Autowired
    public SuggestService(SuggestRepository suggestRepository) {
        this.suggestRepository = suggestRepository;
    }

    public List<Suggest> getSuggestByGroupId(Long group_id){ return suggestRepository.findByGroupId(group_id); }
    public Suggest getSuggestById(Long suggest_id){ return suggestRepository.findById(suggest_id).orElse(null);}



}
