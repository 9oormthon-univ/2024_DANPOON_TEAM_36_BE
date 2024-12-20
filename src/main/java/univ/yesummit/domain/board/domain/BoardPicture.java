package univ.yesummit.domain.board.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import univ.yesummit.domain.feed.entity.Feed;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardPicture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_picture_id")
    @Schema(description = "게시글의 사진 id", example = "1")
    private Long boardPictureId;

    @Schema(description = "게시글 이미지 URL", example = "https://~~")
    @NotNull(message = "게시글 이미지 삽입은 필수입니다.")
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @Builder
    private BoardPicture(String imageUrl, Board board, Feed feed) {
        this.imageUrl = (feed != null) ? feed.getImage() : imageUrl;
        this.board = board;
        this.feed = feed;
    }
}