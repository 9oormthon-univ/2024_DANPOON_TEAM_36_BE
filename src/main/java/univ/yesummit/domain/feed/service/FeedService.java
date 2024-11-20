package univ.yesummit.domain.feed.service;

import org.springframework.web.multipart.MultipartFile;
import univ.yesummit.domain.feed.dto.FeedListResponse;
import univ.yesummit.domain.member.entity.Member;

import java.util.List;

public interface FeedService {

    List<String> uploadImagesToS3(List<MultipartFile> files, Long memberId);

    FeedListResponse getAllFeedsOfMember(Long memberId);

    void deleteFeed(Long feedId, Long memberId);
}
