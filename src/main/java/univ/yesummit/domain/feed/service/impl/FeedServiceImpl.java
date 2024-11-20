package univ.yesummit.domain.feed.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import univ.yesummit.domain.feed.dto.FeedListResponse;
import univ.yesummit.domain.feed.dto.FeedResponse;
import univ.yesummit.domain.feed.entity.Feed;
import univ.yesummit.domain.feed.repository.FeedRepository;
import univ.yesummit.domain.feed.service.FeedService;
import univ.yesummit.domain.member.entity.Member;
import univ.yesummit.domain.member.exception.MemberException;
import univ.yesummit.domain.member.repository.MemberRepository;
import univ.yesummit.global.exception.ErrorCode;
import univ.yesummit.global.s3.service.S3Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class FeedServiceImpl implements FeedService {


        private final FeedRepository feedRepository;
        private final MemberRepository memberRepository;
        private final S3Service s3Service;

        @Override
        public List<String> uploadImagesToS3(List<MultipartFile> files, Long memberId) {
            Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberException(ErrorCode.NOT_FOUND_MEMBER));
            List<String> imageUrls = new ArrayList<>();
            for (MultipartFile file : files) {
                String imageUrl = s3Service.uploadImage(file, "feed-images");
                imageUrls.add(imageUrl);

                Feed feed = Feed.builder()
                        .image(imageUrl)
                        .member(member)
                        .build();
                feedRepository.save(feed);
            }
            return imageUrls;
        }

        @Override
        public FeedListResponse getAllFeedsOfMember(Long memberId) {
            Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberException(ErrorCode.NOT_FOUND_MEMBER));
            List<Feed> feeds = feedRepository.findAllByMember(member);
            List<FeedResponse> feedResponses = feeds.stream()
                    .map(feed -> new FeedResponse(Collections.singletonList(feed.getImage())))
                    .collect(Collectors.toList());
            return new FeedListResponse(feedResponses);
        }

    @Override
    public void deleteFeed(Long feedId, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberException(ErrorCode.NOT_FOUND_MEMBER));
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new RuntimeException("피드를 찾을 수 없습니다."));

        // 피드가 해당 회원의 것인지 확인
        if (!feed.getMember().getId().equals(member.getId())) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        // S3에서 이미지 삭제
        s3Service.deleteImage(feed.getImage());

        // 데이터베이스에서 피드 삭제
        feedRepository.delete(feed);
    }

}


