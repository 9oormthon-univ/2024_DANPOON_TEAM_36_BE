package univ.yesummit.domain.comment.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import univ.yesummit.domain.board.domain.Board;
import univ.yesummit.domain.member.entity.Member;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "댓글", example = "댓글")
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member writer;

    @Builder
    private Comment(String comment, Member writer, Board board) {
        this.comment = comment;
        this.writer = writer;
        this.board = board;
    }

    public void updateComment(String comment) {

        if (comment == null || comment.trim().isEmpty()) {
            throw new IllegalArgumentException("댓글 내용은 비어 있을 수 없습니다.");
        }
        if (this.comment.equals(comment)) {
            return;
        }
        this.comment = comment;

        System.out.println("댓글이 수정되었습니다: " + comment);
    }
}
