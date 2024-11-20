package univ.yesummit.domain.feed.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import univ.yesummit.domain.feed.dto.FeedListResponse;
import univ.yesummit.domain.feed.dto.FeedResponse;
import univ.yesummit.domain.feed.service.FeedService;
import univ.yesummit.global.exception.dto.ResponseVO;
import univ.yesummit.global.resolver.LoginUser;
import univ.yesummit.global.resolver.User;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/feed")
public class FeedController {

    private final FeedService feedService;


    @Operation(summary = "S3 버킷에 이미지를 업로드합니다.", description = "이미지 업로드 완료 후, 각각 이미지의 URL LIST를 JSON 형식으로 반환합니다.")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO<FeedResponse> uploadImage(
            @Parameter(description = "multipart/form-data 형식의 이미지 리스트를 input으로 받습니다. 이때 key 값은 files입니다.")
            @RequestParam("files") List<MultipartFile> files,
            @User LoginUser loginUser) {
        Long memberId = loginUser.getMemberId();

        List<String> imageUrls = feedService.uploadImagesToS3(files, memberId);
        return new ResponseVO<>(new FeedResponse(imageUrls));
    }

    @GetMapping
    @Operation(summary = "사용자의 모든 피드 조회", description = "사용자의 모든 피드를 조회하고 해당 리스트를 반환합니다.")
    public ResponseVO<FeedListResponse> getAllFeeds(@User LoginUser loginUser) {
        Long memberId = loginUser.getMemberId();
        FeedListResponse feedListResponse = feedService.getAllFeedsOfMember(memberId);
        return new ResponseVO<>(feedListResponse);
    }

    @DeleteMapping("/{feedId}")
    @Operation(summary = "피드 및 이미지 삭제", description = "피드와 해당 이미지를 삭제합니다.")
    public ResponseVO<String> deleteFeed(@PathVariable Long feedId, @User LoginUser loginUser) {
        Long memberId = loginUser.getMemberId();
        feedService.deleteFeed(feedId, memberId);
        return new ResponseVO<>("피드가 성공적으로 삭제되었습니다.");
    }
}
