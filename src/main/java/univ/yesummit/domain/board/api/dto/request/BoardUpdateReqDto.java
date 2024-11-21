package univ.yesummit.domain.board.api.dto.request;

import java.util.List;

public record BoardUpdateReqDto(
        String title,
        String content,
        List<String> newImageUrl,
        String serviceUrl,
        String PTUrl
) {
}
