package univ.yesummit.domain.comment.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import univ.yesummit.domain.comment.api.dto.request.CommentSaveReqDto;
import univ.yesummit.domain.comment.api.dto.request.CommentUpdateReqDto;
import univ.yesummit.domain.comment.application.CommentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/comment")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 등록", description = "댓글을 등록합니다.")
    @ApiResponse(responseCode = "200", description = "댓글 등록 성공")
    @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    @PostMapping("/")
    public ResponseEntity<String> commentSave(@AuthenticationPrincipal String email,
                                              @RequestBody CommentSaveReqDto commentSaveReqDto) {

        commentService.commentSave(email, commentSaveReqDto);

        String responseMessage = String.format("%d 게시글에 댓글이 등록되었습니다.", commentSaveReqDto.boardId());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
    }

    @Operation(summary = "댓글 수정", description = "댓글을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "댓글 수정 성공")
    @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    @PutMapping("/{commentId}")
    public ResponseEntity<String> commentUpdate(@AuthenticationPrincipal String email,
                                                @PathVariable("commentId") Long commentId,
                                                @RequestBody CommentUpdateReqDto commentUpdateReqDto) {

        commentService.commentUpdate(email, commentId, commentUpdateReqDto);

        String responseMessage = String.format("%d 게시글의 댓글이 수정돠었습니다.", commentId);
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "댓글 삭제 성공")
    @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> commentDelete(@AuthenticationPrincipal String email,
                                                @PathVariable("commentId") Long commentId) {
        commentService.commentDelete(email, commentId);

        String responseMessage = String.format("%d 게시글의 댓글이 삭제되었습니다.", commentId);
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }
}
