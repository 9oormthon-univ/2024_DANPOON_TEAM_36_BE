package univ.yesummit.domain.board.api.dto.response;

public record InvestmentResDto(
        Long myMemberId,
        String username,
        String position,
        String phoneNumber,
        String email,
        Long boardId,
        String boardTitle,
        String boardContent,
        int investmentCount
) {
    public static InvestmentResDto
    of(Long myMemberId,
       String username,
       String position,
       String phoneNumber,
       String email,
       Long boardId,
       String boardTitle,
       String boardContent,
       int investmentCount)

    {
        return new InvestmentResDto(
                myMemberId,
                username,
                position,
                phoneNumber,
                email,
                boardId,
                boardTitle,
                boardContent,
                investmentCount);
    }
}