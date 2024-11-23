package univ.yesummit.domain.board.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import univ.yesummit.domain.board.api.dto.response.InvestmentResDto;
import univ.yesummit.domain.board.application.InvestmentService;
import univ.yesummit.domain.board.domain.Board;
import univ.yesummit.domain.board.domain.repository.BoardRepository;
import univ.yesummit.domain.member.entity.Member;
import univ.yesummit.domain.member.repository.MemberRepository;
import univ.yesummit.global.resolver.LoginUser;
import univ.yesummit.global.resolver.User;

import java.util.List;

@RestController
@RequestMapping("/v1/api/board/investment")
@RequiredArgsConstructor
public class InvestmentController {

    private final InvestmentService investmentService;

    @Operation(summary = "투자 제안하기", description = "특정 게시글에 투자 제안을 등록합니다.")
    @PostMapping("/{boardId}")
    public ResponseEntity<String> addInvestment(
            @User LoginUser loginUser,
            @PathVariable Long boardId) {

        investmentService.addInvestment(loginUser.getMemberId(), boardId);
        return ResponseEntity.ok("investment ok");
    }

    @Operation(summary = "투자 제안 취소", description = "특정 게시글의 투자 제안을 취소합니다.")
    @DeleteMapping("/{boardId}")
    public ResponseEntity<String> cancelInvestment(
            @User LoginUser loginUser,
            @PathVariable Long boardId) {

        investmentService.cancelInvestment(loginUser.getMemberId(), boardId);
        return ResponseEntity.ok("cancel investment ok");
    }

    @Operation(summary = "내가 투자한 게시글 조회", description = "특정 사용자가 투자한 게시글 리스트를 조회합니다.")
    @GetMapping("/my-investments")
    public ResponseEntity<List<InvestmentResDto>> getMyInvestments(@User LoginUser loginUser) {
        List<InvestmentResDto> investments = investmentService.getInvestedBoardsWithDetailsByMember(loginUser.getMemberId());
        return ResponseEntity.ok(investments);
    }
}

//
//    @Operation(summary = "투자 제안하기", description = "투자 제안을 등록합니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "투자 제안하기 성공"),
//            @ApiResponse(responseCode = "401", description = "인증실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
//            @ApiResponse(responseCode = "404", description = "게시글 또는 회원을 찾을 수 없음", content = @Content(schema = @Schema(example = "BOARD_NOT_FOUND or MEMBER_NOT_FOUND"))),
//    })
//    @PostMapping(value = "/invest", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public ResponseEntity<String> addBoardLike(@User LoginUser loginUser, @RequestParam Long boardId) {
//        Member member = memberRepository.findById(loginUser.getMemberId())
//                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
//        Board board = boardRepository.findById(boardId)
//                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
//
//        investmentService.addInvestment(loginUser.getMemberId(), board);
//        return ResponseEntity.ok("투자 제안하기");
//    }
//
//    @Operation(summary = "투자 제안하기 취소", description = "투자 제안을 취소합니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "투자 제안하기 취소 성공"),
//            @ApiResponse(responseCode = "401", description = "인증실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
//            @ApiResponse(responseCode = "404", description = "게시글 또는 회원을 찾을 수 없음", content = @Content(schema = @Schema(example = "BOARD_NOT_FOUND or MEMBER_NOT_FOUND"))),
//    })
//    @PostMapping(value = "/cancel", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public ResponseEntity<String> cancelBoardLike(@User LoginUser loginUser, @RequestParam Long boardId) {
//        Member member = memberRepository.findById(loginUser.getMemberId())
//                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
//        Board board = boardRepository.findById(boardId)
//                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
//
//        investmentService.cancelInvestment(loginUser.getMemberId(), board);
//        return ResponseEntity.ok("투자 제안하기 취소");
//    }
//
//    @Operation(summary = "내가 받은 투자 제안 조회", description = "내가 받은 투자 제안을 조회합니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "내가 받은 투자 제안 조회 성공"),
//            @ApiResponse(responseCode = "401", description = "인증실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
//            @ApiResponse(responseCode = "404", description = "게시글 또는 회원을 찾을 수 없음", content = @Content(schema = @Schema(example = "BOARD_NOT_FOUND or MEMBER_NOT_FOUND"))),
//    })
//    @GetMapping("/get-investment")
//    public ResponseEntity<List<InvestmentResDto>> getAllInvestment(@User LoginUser loginUser, @RequestParam Long boardId) {
//        List<InvestmentResDto> investments = investmentService.getAllInvestment(loginUser.getMemberId(), boardId);
//        return ResponseEntity.ok(investments);
//    }
//
//}

