package univ.yesummit.domain.board.api.dto.request;

import univ.yesummit.domain.board.domain.Board;
import univ.yesummit.domain.member.entity.Member;

import java.util.List;

public record BoardSaveReqDto(
        String title,
        String content,
        List<String> imageUrl,
        String serviceUrl,
        String PTUrl
) {
    public Board toEntity(Member member) {
        return Board.builder()
                .title(title)
                .content(content)
                .writer(member)
                .serviceUrl(serviceUrl)
                .PTUrl(PTUrl)
                .build();
    }
}
