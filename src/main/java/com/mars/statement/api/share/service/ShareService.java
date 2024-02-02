package com.mars.statement.api.share.service;

import com.mars.statement.api.chapter.domain.Chapter;
import com.mars.statement.api.chapter.service.ChapterService;
import com.mars.statement.api.chapter.service.SuggestService;
import com.mars.statement.api.group.service.GroupMemberService;
import com.mars.statement.api.share.dto.MemberOpinionDto;
import com.mars.statement.api.share.dto.OpinionDto;
import com.mars.statement.api.share.dto.PersonalShareDto;
import com.mars.statement.api.share.repository.ShareRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShareService {

    private final ModelMapper modelMapper;

    private final GroupMemberService groupMemberService;
    private final SuggestService suggestService;
    private final ChapterService chapterService;

    private final ShareRepository shareRepository;

    public List<PersonalShareDto> getPersonalShareData(Long group_id, Long suggest_id, Long my_id) {
        List<Chapter> chapters = chapterService.getChaptersByMemberId(group_id, my_id, suggest_id);
        List<Long> chapterIds = chapters.stream().map(Chapter::getId).toList();

        List<PersonalShareDto> personalShares = shareRepository.findPersonalSharesByIds(chapterIds);

        return personalShares.stream()
                .collect(Collectors.groupingBy(
                        PersonalShareDto::getSuggestId,
                        Collectors.groupingBy(
                                PersonalShareDto -> PersonalShareDto.getMemberOpinionDto().getMemberId(),
                                Collectors.toList()
                        )
                ))
                .entrySet().stream()
                .flatMap(suggestEntry -> suggestEntry.getValue().entrySet().stream()
                        .map(memberEntry -> {
                            Long suggestId = suggestEntry.getKey();
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

                            return new PersonalShareDto(suggestId, PersonalShareDtos.get(0).getSuggest(), mergedMemberOpinionDto);
                        }))
                .toList();
    }


}
