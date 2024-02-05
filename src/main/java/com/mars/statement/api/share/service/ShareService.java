package com.mars.statement.api.share.service;
import com.mars.statement.api.chapter.domain.Chapter;
import com.mars.statement.api.chapter.domain.ChapterMember;
import com.mars.statement.api.chapter.domain.Suggest;
import com.mars.statement.api.chapter.dto.ChapterDTO;
import com.mars.statement.api.chapter.repository.ChapterMemberRepository;
import com.mars.statement.api.chapter.service.ChapterService;
import com.mars.statement.api.chapter.service.SuggestService;
import com.mars.statement.api.group.domain.GroupMember;
import com.mars.statement.api.group.service.GroupMemberService;
import com.mars.statement.api.share.domain.Share;
import com.mars.statement.api.share.dto.MemberOpinionDTO;
import com.mars.statement.api.share.dto.OpinionDTO;
import com.mars.statement.api.share.dto.ShareDTO;
import com.mars.statement.api.share.repository.ShareRepository;
import com.mars.statement.global.dto.CommonResponse;
import com.mars.statement.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShareService {

    private final GroupMemberService groupMemberService;

    private final SuggestService suggestService;
    private final ShareRepository shareRepository;

    private final ChapterService chapterService;
    private final ModelMapper modelMapper;

    private final ChapterMemberRepository chapterMemberRepository;

    @Transactional
    public ResponseEntity<?> insertShare(ShareDto shareDto) throws NotFoundException {
        Long chapterMemberId = shareDto.getChapterMemberId();
        Optional<ChapterMember> optionalChapterMember = chapterMemberRepository.findById(chapterMemberId);

        if (optionalChapterMember.isEmpty()) {
            log.warn("존재하지 않는 ChapterMember입니다");
            throw new NotFoundException("존재하지 않는 ChapterMember입니다.");
        }

        ChapterMember chapterMember = optionalChapterMember.get();

        // Share 엔티티를 데이터베이스에 저장
        shareRepository.save(Share.builder()
                .chapterMember(chapterMember)
                .opinion(shareDto.getOpinion())
                .location(shareDto.getLocation())
                .build());

        // createResponseMessage 메소드를 사용하여 응답 생성
        return CommonResponse.createResponseMessage(HttpStatus.OK.value(), "의견 작성 성공");
    }

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

}