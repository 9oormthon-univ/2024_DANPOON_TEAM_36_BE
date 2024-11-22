package univ.yesummit.domain.board.api.dto.response;

public record InvestmentResDto(
        Long myMemberId,
        String username,
        String position,
        String phoneNumber,
        String email,
        Long boardId
) {
}
