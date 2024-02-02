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

    private final GroupMemberService groupMemberService;

    private final SuggestService suggestService;
    private final ShareRepository shareRepository;

    private final ChapterService chapterService;
    private final ModelMapper modelMapper;
    public List<PersonalShareDto> getPersonalShareData(Long groupId, Long suggestId, Long myId){

        List<Chapter> chapters = chapterService.getChaptersByMemberId(groupId, myId, suggestId);
        List<Long> chapterIds = chapters.stream().map(Chapter::getId).toList();

        for(Long id : chapterIds){
            System.out.println(id);
        }

        List<PersonalShareDto> personalShares = shareRepository.findPersonalSharesByIds(chapterIds);

        return personalShares.stream()
                .collect(Collectors.groupingBy(
                        PersonalShareDto::getSuggestId,
                        Collectors.groupingBy(
                                personalShareDto -> personalShareDto.getMemberOpinionDtoList().get(0).getMemberId(),
                                Collectors.toList()
                        )
                ))
                .entrySet().stream()
                .flatMap(suggestEntry -> suggestEntry.getValue().entrySet().stream()
                        .map(memberEntry -> {
                            Long sId = suggestEntry.getKey();
                            Long memberId = memberEntry.getKey();
                            List<PersonalShareDto> personalShareDto = memberEntry.getValue();

                            List<OpinionDto> opinionDtoList = personalShareDto.stream()
                                    .flatMap(dto -> dto.getMemberOpinionDtoList().stream())
                                    .map(MemberOpinionDto::getOpinionDtoList)
                                    .flatMap(List::stream)
                                    .collect(Collectors.toList());

                            MemberOpinionDto mergedMemberOpinionDto = new MemberOpinionDto(memberId,
                                    personalShareDto.get(0).getMemberOpinionDtoList().get(0).getMemberName(),
                                    personalShareDto.get(0).getMemberOpinionDtoList().get(0).getMemberImg(), opinionDtoList);



                            return new PersonalShareDto(sId, personalShareDto.get(0).getSuggest(), mergedMemberOpinionDto);
                        }))
                .toList();
    }

}
