package com.mars.statement.api.share.service;

import com.mars.statement.api.chapter.domain.Chapter;
import com.mars.statement.api.chapter.domain.Suggest;
import com.mars.statement.api.chapter.dto.ChapterDTO;
import com.mars.statement.api.chapter.service.ChapterService;
import com.mars.statement.api.chapter.service.SuggestService;
import com.mars.statement.api.group.domain.GroupMember;
import com.mars.statement.api.group.service.GroupMemberService;
import com.mars.statement.api.share.domain.Share;
import com.mars.statement.api.share.dto.MemberOpinionDTO;
import com.mars.statement.api.share.dto.OpinionDTO;
import com.mars.statement.api.share.dto.ShareDTO;
import com.mars.statement.api.share.repository.ShareRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ShareService {

    private final GroupMemberService groupMemberService;

    private final SuggestService suggestService;
    private final ShareRepository shareRepository;

    private final ChapterService chapterService;
    private final ModelMapper modelMapper;

    public ShareService(GroupMemberService groupMemberService, SuggestService suggestService, ShareRepository shareRepository,
                        ChapterService chapterService, ModelMapper modelMapper){
        this.groupMemberService = groupMemberService;
        this.suggestService = suggestService;
        this.shareRepository = shareRepository;
        this.chapterService = chapterService;
        this.modelMapper = modelMapper;
    }

    public List<ShareDTO> getPersonalShareData(Long group_id, Long suggest_id, Long my_id){

        List<Chapter> chapters = chapterService.getChaptersByMemberId(group_id, my_id, suggest_id);
        List<Long> chapterIds = chapters.stream().map(Chapter::getId).toList();


        List<ShareDTO> personalShares = shareRepository.findPersonalSharesByIds(chapterIds);

        return personalShares.stream()
                .collect(Collectors.groupingBy(
                        ShareDTO::getSuggestId,
                        Collectors.groupingBy(
                                shareDTO -> shareDTO.getMemberOpinionDTO().getMemberId()
                        )
                ))
                .entrySet().stream()
                .flatMap(suggestEntry -> suggestEntry.getValue().entrySet().stream()
                        .map(memberEntry -> {
                            Long suggestId = suggestEntry.getKey();
                            Long memberId = memberEntry.getKey();
                            List<ShareDTO> shareDTOs = memberEntry.getValue();

                            List<OpinionDTO> opinionDTOList = shareDTOs.stream()
                                    .map(ShareDTO::getMemberOpinionDTO)
                                    .map(MemberOpinionDTO::getOpinionDTO)
                                    .collect(Collectors.toList());

                            MemberOpinionDTO mergedMemberOpinionDTO = new MemberOpinionDTO(memberId,
                                    shareDTOs.get(0).getMemberOpinionDTO().getMemberName(),
                                    shareDTOs.get(0).getMemberOpinionDTO().getMemberImg(), opinionDTOList);

                            return new ShareDTO(suggestId, shareDTOs.get(0).getSuggest(), mergedMemberOpinionDTO);
                        }))
                .toList();
    }

}
