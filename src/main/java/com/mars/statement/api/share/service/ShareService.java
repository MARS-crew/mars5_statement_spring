package com.mars.statement.api.share.service;

import com.mars.statement.api.chapter.domain.Chapter;
import com.mars.statement.api.chapter.domain.ChapterMember;
import com.mars.statement.api.chapter.dto.CheckChapterDto;
import com.mars.statement.api.chapter.dto.ChapterSummaryDto;
import com.mars.statement.api.chapter.service.ChapterMemberService;
import com.mars.statement.api.chapter.service.ChapterService;
import com.mars.statement.api.chapter.service.SuggestService;
import com.mars.statement.api.group.domain.GroupMember;
import com.mars.statement.api.group.service.GroupMemberService;
import com.mars.statement.api.share.domain.Share;
import com.mars.statement.api.share.dto.*;
import com.mars.statement.api.share.repository.ShareRepository;
import com.mars.statement.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShareService {

    private final ShareRepository shareRepository;
    private final GroupMemberService groupMemberService;
    private final SuggestService suggestService;
    private final ChapterService chapterService;
    private final ChapterMemberService chapterMemberService;

    public Share getShareById(Long shareId) throws NotFoundException {
        return shareRepository.findById(shareId).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND.value(), "공유 정보를 찾을 수 없습니다."));
    }

    public PersonalShareDto getPersonalShareData(Long suggestId, Long myId) {
        List<Chapter> chapters = chapterService.getChaptersByMemberId(myId, suggestId);
        List<Long> chapterIds = chapters.stream().map(Chapter::getId).toList();

        List<PersonalShareDto> personalShares = shareRepository.findPersonalSharesByIds(chapterIds);

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
                            .collect(Collectors.toList());  // 수정된 부분

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

    public CheckChapterDto getChapterShareData(Long suggestId, Long myId) {
        List<Chapter> chapters = chapterService.getChaptersByMemberId(myId, suggestId);
        List<Long> chapterIds = chapters.stream().map(Chapter::getId).toList();

        List<CheckChapterDto> checkChapterDtoList = shareRepository.findChapterSharesByIds(chapterIds);

        List<ChapterSummaryDto> allChapterSummaryDtoList = new ArrayList<>();

        for (CheckChapterDto checkChapterDto : checkChapterDtoList) {
            allChapterSummaryDtoList.add(checkChapterDto.getChapterSummaryDto());
        }
        return new CheckChapterDto(checkChapterDtoList.get(0).getSuggestId(), checkChapterDtoList.get(0).getSuggest(), allChapterSummaryDtoList);
    }

    public ShareDetailDto getShareDetails(Long chapterId, Long myId) {
        Chapter chapter = chapterService.getChapterById(chapterId);
        ChapterMember member = chapterMemberService.getChapterMemberByChapterIdAndUserId(chapter.getId(), myId);

        List<ShareDetailDto> shareDetailList = shareRepository.findShareDetails(chapter.getId(), member.getId());

        List<ShareMemberDetailDto> allChapterMemberDetailList = new ArrayList<>();
        for (ShareDetailDto shareDetailDto : shareDetailList) {
            allChapterMemberDetailList.add(shareDetailDto.getShareMemberDetailDto());
        }
        return new ShareDetailDto(shareDetailList.get(0).getSuggestId(), shareDetailList.get(0).getSuggest(),
                shareDetailList.get(0).getChapterId(), shareDetailList.get(0).getSummary(),
                allChapterMemberDetailList);

    }

}
