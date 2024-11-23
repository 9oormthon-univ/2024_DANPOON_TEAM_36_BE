package univ.yesummit.domain.board.api.dto.request;

import univ.yesummit.domain.board.domain.Board;
import univ.yesummit.domain.board.domain.BoardPicture;
import univ.yesummit.domain.member.entity.Member;

import java.util.List;

public record BoardSaveReqDto(
        String title,
        String content,
        List<String> imageUrl,
        String serviceUrl,
        String PTUrl
) {
    public BoardSaveReqDto {
        if (imageUrl == null) {
            throw new IllegalArgumentException("이미지 URL null");
        }
    }
    public Board toEntity(Member member) {
        List<BoardPicture> boardPictures = imageUrl.stream()
                .map(url -> BoardPicture.builder()
                        .imageUrl(url)
                        .build())
                .toList();

        return Board.builder()
                .title(title)
                .content(content)
                .pictures(boardPictures)
                .writer(member)
                .serviceUrl(serviceUrl)
                .PTUrl(PTUrl)
                .build();
    }

}
