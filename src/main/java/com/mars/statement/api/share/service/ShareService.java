package com.mars.statement.api.share.service;

import com.mars.statement.api.chapter.domain.Chapter;
import com.mars.statement.api.chapter.dto.CheckChapterDto;
import com.mars.statement.api.chapter.dto.ChapterSummaryDto;
import com.mars.statement.api.chapter.service.ChapterService;
import com.mars.statement.api.chapter.service.SuggestService;
import com.mars.statement.api.group.service.GroupMemberService;
import com.mars.statement.api.share.dto.*;
import com.mars.statement.api.share.repository.ShareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShareService {

    private final GroupMemberService groupMemberService;

    private final SuggestService suggestService;
    private final ShareRepository shareRepository;

    private final ChapterService chapterService;

    public List<PersonalShareDto> getPersonalShareData(Long suggestId, Long myId) {
        List<Chapter> chapters = chapterService.getChaptersByMemberId(myId, suggestId);
        List<Long> chapterIds = chapters.stream().map(Chapter::getId).toList();

        List<PersonalShareDto> personalShares = shareRepository.findPersonalSharesByIds(chapterIds);

        return personalShares.stream()
                .collect(Collectors.groupingBy(
                        PersonalShareDto::getSuggestId,
                        Collectors.groupingBy(
                                PersonalShareDto -> PersonalShareDto.getOpinionList().get(0).getMemberId(),
                                Collectors.toList()
                        )
                ))
                .entrySet().stream()
                .flatMap(suggestEntry -> suggestEntry.getValue().entrySet().stream()
                        .map(memberEntry -> {
                            Long sId = suggestEntry.getKey();
                            Long memberId = memberEntry.getKey();
                            List<PersonalShareDto> PersonalShareDtos = memberEntry.getValue();

                            List<OpinionDto> opinionDtoList = PersonalShareDtos.stream()
                                    .flatMap(dto -> dto.getOpinionList().stream())
                                    .map(MemberOpinionDto::getOpinionList)
                                    .flatMap(List::stream)
                                    .collect(Collectors.toList());

                            MemberOpinionDto mergedMemberOpinionDto = new MemberOpinionDto(memberId,
                                    PersonalShareDtos.get(0).getMemberOpinionDto().getMemberName(),
                                    PersonalShareDtos.get(0).getMemberOpinionDto().getMemberImg(), opinionDtoList);

                            return new PersonalShareDto(sId, PersonalShareDtos.get(0).getSuggest(), mergedMemberOpinionDto);

                        }))
                .toList();
    }

    public CheckChapterDto getChapterShareData(Long suggestId, Long myId) {
        List<Chapter> chapters = chapterService.getChaptersByMemberId(myId, suggestId);
        List<Long> chapterIds = chapters.stream().map(Chapter::getId).toList();

        List<CheckChapterDto> checkChapterDtoList = shareRepository.findChapterSharesByIds(chapterIds);

        List<ChapterSummaryDto> allChapterSummaryDtoList = new ArrayList<>();

        for (CheckChapterDto checkChapterDto : checkChapterDtoList) {
            allChapterSummaryDtoList.add(checkChapterDto.getChapterSummaryDto());
        }
        return new CheckChapterDto(checkChapterDtoList.get(0).getSuggestId(), checkChapterDtoList.get(0).getSuggest(),allChapterSummaryDtoList);
    }

}
