package univ.yesummit.domain.board.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import univ.yesummit.domain.board.api.dto.response.BoardLikeResDto;
import univ.yesummit.domain.board.application.BoardLikeService;
import univ.yesummit.domain.board.domain.Board;
import univ.yesummit.domain.board.domain.repository.BoardRepository;
import univ.yesummit.domain.member.entity.Member;
import univ.yesummit.domain.member.repository.MemberRepository;
import univ.yesummit.global.resolver.LoginUser;
import univ.yesummit.global.resolver.User;

import java.util.List;

@RestController
@RequestMapping("/v1/api/board/like")
public class BoardLikeController {
    private final BoardLikeService boardLikeService;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public BoardLikeController(BoardLikeService boardLikeService, BoardRepository boardRepository, MemberRepository memberRepository) {
        this.boardLikeService = boardLikeService;
        this.boardRepository = boardRepository;
        this.memberRepository = memberRepository;
    }

    @Operation(summary = "PT 영상 게시글 좋아요", description = "PT 영상 게시글 좋아요를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 좋아요 성공"),
            @ApiResponse(responseCode = "401", description = "인증실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
            @ApiResponse(responseCode = "404", description = "게시글 또는 회원을 찾을 수 없음", content = @Content(schema = @Schema(example = "BOARD_NOT_FOUND or MEMBER_NOT_FOUND"))),
    })
    @PostMapping(value = "/like", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> addBoardLike(@User LoginUser loginUser, @RequestParam Long boardId) {
        Member member = memberRepository.findById(loginUser.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        boardLikeService.addBoardLike(loginUser.getMemberId(), board);
        return ResponseEntity.ok("게시글 좋아요");
    }

    @Operation(summary = "PT 영상 게시글 좋아요 취소", description = "PT 영상 게시글 좋아요를 취소합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 좋아요 취소 성공"),
            @ApiResponse(responseCode = "401", description = "인증실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
            @ApiResponse(responseCode = "404", description = "게시글 또는 회원을 찾을 수 없음", content = @Content(schema = @Schema(example = "BOARD_NOT_FOUND or MEMBER_NOT_FOUND"))),
    })
    @PostMapping(value = "/cancel", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> cancelBoardLike(@User LoginUser loginUser, @RequestParam Long boardId) {
        Member member = memberRepository.findById(loginUser.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        boardLikeService.cancelBoardLike(loginUser.getMemberId(), board);
        return ResponseEntity.ok("게시글 좋아요 취소");
    }

    // 내가 좋아요 누른 게시글 조회
    @Operation(summary = "내가 좋아요 누른 게시글 조회", description = "사용자가 좋아요를 누른 모든 게시글을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
            @ApiResponse(responseCode = "404", description = "회원 정보를 찾을 수 없음", content = @Content(schema = @Schema(example = "MEMBER_NOT_FOUND"))),
    })
    @GetMapping("/my/likes")
    public ResponseEntity<List<BoardLikeResDto>> getMyLikedBoards(@User LoginUser loginUser) {
        List<BoardLikeResDto> likedBoards = boardLikeService.findMyLikedBoards(loginUser.getMemberId());
        return ResponseEntity.ok(likedBoards);
    }

}
