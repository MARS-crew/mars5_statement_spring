package com.mars.statement.api.chapter.service;

import com.mars.statement.api.chapter.domain.Suggest;
import com.mars.statement.api.chapter.repository.SuggestRepository;
import com.mars.statement.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class SuggestService {

    private final SuggestRepository suggestRepository;


    public List<Suggest> getSuggestByGroupId(Long groupId){ return suggestRepository.findByGroupId(groupId); }
    public Suggest getSuggestById(Long suggestId) throws NotFoundException {
        Optional<Suggest> optionalSuggest = suggestRepository.findById(suggestId);

        if (optionalSuggest.isEmpty()) {
            throw new NotFoundException(HttpStatus.NOT_FOUND.value(), "해당 제안 정보를 찾을 수 없습니다.");
        }

        return optionalSuggest.get();
    }


}
