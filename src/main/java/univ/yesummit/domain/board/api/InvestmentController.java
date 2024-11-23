package univ.yesummit.domain.board.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import univ.yesummit.domain.board.api.dto.response.InvestmentReceivedResDto;
import univ.yesummit.domain.board.api.dto.response.InvestmentResDto;
import univ.yesummit.domain.board.application.InvestmentService;
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

    @Operation(summary = "내가 받은 투자 보기", description = "")
    @GetMapping("/received-list")
    public ResponseEntity<List<InvestmentReceivedResDto>> getReceivedInvestments(@User LoginUser loginUser) {
        List<InvestmentReceivedResDto> investments = investmentService.getReceivedInvestments(loginUser.getMemberId());
        return ResponseEntity.ok(investments);
    }
}