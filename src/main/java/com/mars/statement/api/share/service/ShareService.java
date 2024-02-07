package com.mars.statement.api.share.service;

import com.mars.statement.api.chapter.domain.Chapter;
import com.mars.statement.api.chapter.domain.ChapterMember;
import com.mars.statement.api.chapter.domain.Suggest;
import com.mars.statement.api.chapter.dto.CheckChapterDto;
import com.mars.statement.api.chapter.dto.ChapterSummaryDto;
import com.mars.statement.api.chapter.repository.ChapterMemberRepository;
import com.mars.statement.api.chapter.repository.ChapterRepository;
import com.mars.statement.api.chapter.service.ChapterMemberService;
import com.mars.statement.api.chapter.service.ChapterService;
import com.mars.statement.api.chapter.service.SuggestService;
import com.mars.statement.api.share.domain.Share;
import com.mars.statement.api.share.dto.*;
import com.mars.statement.api.share.repository.ShareRepository;
import com.mars.statement.global.dto.CommonResponse;
import com.mars.statement.global.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShareService {

    private final ShareRepository shareRepository;
    private final ChapterRepository chapterRepository;
    private final ChapterService chapterService;
    private final ChapterMemberService chapterMemberService;
    private final ChapterMemberRepository chapterMemberRepository;
    private final SuggestService suggestService;

    public Share getShareById(Long shareId) throws NotFoundException {
        return shareRepository.findById(shareId).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND.value(), "공유 정보를 찾을 수 없습니다."));
    }

    public PersonalShareDto getPersonalShareData(Long suggestId, Long myId) throws NotFoundException {
        Suggest suggest = suggestService.getSuggestById(suggestId);

        List<Chapter> chapters = chapterService.getChaptersByMemberId(myId, suggest);
        List<Long> chapterIds = chapters.stream().map(Chapter::getId).toList();

        List<PersonalShareDto> personalShares = shareRepository.findPersonalSharesByIds(chapterIds);

        if (personalShares.isEmpty()) {
            throw new NotFoundException(HttpStatus.NOT_FOUND.value(), "해당 공유 정보를 찾을 수 없습니다.");
        }

        List<MemberOpinionDto> allMemberOpinionList = personalShares.stream()
                .collect(Collectors.groupingBy(
                        personalShareDto -> personalShareDto.getMemberOpinionDto().getMemberId(),
                        Collectors.mapping(
                                PersonalShareDto::getOpinionList,
                                Collectors.toList()
                        )
                ))
                .entrySet().stream()
                .map(memberEntry -> {
                    Long memberId = memberEntry.getKey();
                    List<OpinionDto> opinionList = memberEntry.getValue().stream()
                            .flatMap(List::stream)
                            .flatMap(dto -> dto.getOpinionList().stream())
                            .collect(Collectors.toList());

                    MemberOpinionDto memberOpinionDto = personalShares.stream()
                            .filter(dto -> dto.getMemberOpinionDto().getMemberId().equals(memberId))
                            .findFirst()
                            .map(PersonalShareDto::getMemberOpinionDto)
                            .orElseThrow();
                    return new MemberOpinionDto(memberOpinionDto.getMemberId(), memberOpinionDto.getMemberName(),
                            memberOpinionDto.getMemberImg(), opinionList);
                })
                .toList();
        return new PersonalShareDto(personalShares.get(0).getSuggestId(), personalShares.get(0).getSuggest(), allMemberOpinionList);

    }

    public CheckChapterDto getChapterShareData(Long suggestId, Long myId) throws NotFoundException {

        Suggest suggest = suggestService.getSuggestById(suggestId);
        if(!suggest.getType().equals("share")){
            throw new NotFoundException(HttpStatus.NOT_FOUND.value(), "공유 주제가 아닙니다.");
        }

        List<Chapter> chapters = chapterService.getChaptersByMemberId(myId, suggest);
        List<Long> chapterIds = chapters.stream().map(Chapter::getId).toList();

        List<CheckChapterDto> checkChapterDtoList = shareRepository.findChapterSharesByIds(chapterIds);

        List<ChapterSummaryDto> allChapterSummaryDtoList = new ArrayList<>();

        for (CheckChapterDto checkChapterDto : checkChapterDtoList) {
            allChapterSummaryDtoList.add(checkChapterDto.getChapterSummaryDto());
        }
        return new CheckChapterDto(checkChapterDtoList.get(0).getSuggestId(), checkChapterDtoList.get(0).getSuggest(), allChapterSummaryDtoList);
    }

    public ShareDetailDto getShareDetails(Long chapterId, Long myId) throws NotFoundException {
        Chapter chapter = chapterService.getChapterById(chapterId);
        ChapterMember member = chapterMemberService.getChapterMemberByChapterIdAndUserId(chapter.getId(), myId);

        List<ShareDetailDto> shareDetailList = shareRepository.findShareDetails(chapter.getId(), member.getId());

        if (shareDetailList.isEmpty()) {
            throw new NotFoundException(HttpStatus.NOT_FOUND.value(), "해당 공유 상세 정보를 찾을 수 없습니다.");
        }

        List<ShareMemberDetailDto> allChapterMemberDetailList = new ArrayList<>();
        for (ShareDetailDto shareDetailDto : shareDetailList) {
            allChapterMemberDetailList.add(shareDetailDto.getShareMemberDetailDto());
        }
        return new ShareDetailDto(shareDetailList.get(0).getSuggestId(), shareDetailList.get(0).getSuggest(),
                shareDetailList.get(0).getChapterId(), shareDetailList.get(0).getSummary(),
                allChapterMemberDetailList);

    }

    @Transactional
    public ResponseEntity<?> insertShare(Long chapterId, ShareOpinionDto shareOpinionDto, Long myId) throws NotFoundException {
        // 챕터의 타입이 'share'인지 확인
        String chapterType = chapterRepository.findChapterTypeById(chapterId);

        if (!chapterType.equals("share")) {
            throw new NotFoundException(404, "You can only share opinions in this chapter");
        }

        // 챕터 멤버 확인
        Optional<ChapterMember> myChapterMember = Optional.ofNullable(chapterMemberRepository.findChapterMemberByChapterIdAndUserId(chapterId, myId));
        ChapterMember chapterMember = myChapterMember.orElseThrow(() -> new NotFoundException(404, "Chapter member not found for the current user"));

        // 새로운 Share 엔티티 생성 및 저장
        Share share = Share.builder()
                .chapterMember(chapterMember)
                .opinion(shareOpinionDto.getOpinion())
                .location(shareOpinionDto.getLocation())
                .build();

        shareRepository.save(share);

        // 응답 생성
        return CommonResponse.createResponseMessage(HttpStatus.OK.value(), "의견 작성 성공");
    }


}
