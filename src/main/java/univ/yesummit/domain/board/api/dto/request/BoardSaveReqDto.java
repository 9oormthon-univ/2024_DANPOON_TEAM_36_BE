package univ.yesummit.domain.board.api.dto.request;

import univ.yesummit.domain.board.domain.Board;
import univ.yesummit.domain.board.domain.BoardPicture;
import univ.yesummit.domain.member.entity.Member;

import java.util.List;

import java.util.ArrayList;
public record BoardSaveReqDto(
        String title,
        String content,
        List<String> imageUrl,
        String serviceUrl,
        String PTUrl
) {
    public Board toEntity(Member member) {
        List<String> images = (imageUrl == null) ? new ArrayList<>() : imageUrl;

        // imageUrl 리스트를 이용해 BoardPicture 객체 리스트 생성
        List<BoardPicture> boardPictures = images.stream()
                .map(url -> BoardPicture.builder()
                        .imageUrl(url)
                        .build())
                .toList();

        return Board.builder()
                .title(title)
                .content(content)
                .pictures(boardPictures)
                .serviceUrl(serviceUrl)
                .PTUrl(PTUrl)
                .writer(member)
                .build();
    }
}
