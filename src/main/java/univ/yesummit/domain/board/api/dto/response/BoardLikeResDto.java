package univ.yesummit.domain.board.api.dto.response;

import java.util.List;

public record BoardLikeResDto(
        Long myMemberId,
        Long writerMemberId,
        Long boardId,
        String title,
        String content,
        List<String> imageUrl
) {
}
