package univ.yesummit.domain.board.api.dto.response;

import lombok.Builder;

@Builder
public record InvestmentReceivedResDto(
        Long boardId,           // 게시글 ID
        String boardTitle,      // 게시글 제목
        Long investorId,        // 투자자 ID
        String investorName,    // 투자자 이름
        String investorEmail,    // 투자자 이메일
        String investorPhoneNumber, //투자자 휴대폰번호
        String investorPosition
) {
}