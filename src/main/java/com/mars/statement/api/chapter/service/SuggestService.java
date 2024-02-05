package com.mars.statement.api.chapter.service;

import com.mars.statement.api.chapter.domain.Suggest;
import com.mars.statement.api.chapter.repository.SuggestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SuggestService {

    private final SuggestRepository suggestRepository;

    public List<Suggest> getSuggestByGroupId(Long groupId){ return suggestRepository.findByGroupId(groupId); }
    public Suggest getSuggestById(Long suggestId){ return suggestRepository.findById(suggestId).orElse(null);}

}
