package com.mars.statement.api.share.service;

import com.mars.statement.api.chapter.domain.Chapter;
import com.mars.statement.api.chapter.service.ChapterService;
import com.mars.statement.api.chapter.service.SuggestService;
import com.mars.statement.api.group.service.GroupMemberService;
import com.mars.statement.api.share.dto.*;
import com.mars.statement.api.share.repository.ShareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShareService {

    private final GroupMemberService groupMemberService;

    private final SuggestService suggestService;
    private final ShareRepository shareRepository;

    private final ChapterService chapterService;

    public List<PersonalShareDto> getPersonalShareData(Long groupId, Long suggestId, Long myId) {
        List<Chapter> chapters = chapterService.getChaptersByMemberId(groupId, myId, suggestId);
        List<Long> chapterIds = chapters.stream().map(Chapter::getId).toList();

        List<PersonalShareDto> personalShares = shareRepository.findPersonalSharesByIds(chapterIds);

        return personalShares.stream()
                .collect(Collectors.groupingBy(
                        PersonalShareDto::getSuggestId,
                        Collectors.groupingBy(
                                PersonalShareDto -> PersonalShareDto.getMemberOpinionDtoList().get(0).getMemberId(),
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
                                    .flatMap(dto -> dto.getMemberOpinionDtoList().stream())
                                    .map(MemberOpinionDto::getOpinionDtoList)
                                    .flatMap(List::stream)
                                    .collect(Collectors.toList());

                            MemberOpinionDto mergedMemberOpinionDto = new MemberOpinionDto(memberId,
                                    PersonalShareDtos.get(0).getMemberOpinionDto().getMemberName(),
                                    PersonalShareDtos.get(0).getMemberOpinionDto().getMemberImg(), opinionDtoList);

                            return new PersonalShareDto(sId, PersonalShareDtos.get(0).getSuggest(), mergedMemberOpinionDto);

                        }))
                .toList();
    }

    public ChapterShareDto getChapterShareData(Long groupId, Long suggestId, Long myId) {
        List<Chapter> chapters = chapterService.getChaptersByMemberId(groupId, myId, suggestId);
        List<Long> chapterIds = chapters.stream().map(Chapter::getId).toList();

        List<ChapterShareDto> chapterShareDtoList = shareRepository.findChapterSharesByIds(chapterIds);

        Map<Long, List<ChapterShareDto>> groupedData = chapterShareDtoList.stream()
                .collect(Collectors.groupingBy(ChapterShareDto::getSuggestId));

        List<ChapterShareDto> result = groupedData.entrySet().stream()
                .map(entry -> {
                    Long suggestIdResult = entry.getKey();
                    String suggest = entry.getValue().get(0).getSuggest(); // 가정: 모든 suggest 값이 동일하다고 가정
                    List<ChapterSummaryDto> chapterDtoList = entry.getValue().stream()
                            .map(ChapterShareDto::getChapterSummaryDto)
                            .collect(Collectors.toList());

                    return new ChapterShareDto(suggestIdResult, suggest, chapterDtoList);
                })
                .collect(Collectors.toList());

        return (ChapterShareDto) result;
    }

}
