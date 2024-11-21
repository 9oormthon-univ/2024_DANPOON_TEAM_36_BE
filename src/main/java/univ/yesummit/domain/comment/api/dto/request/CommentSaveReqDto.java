package univ.yesummit.domain.comment.api.dto.request;

import univ.yesummit.domain.board.domain.Board;
import univ.yesummit.domain.comment.domain.Comment;
import univ.yesummit.domain.member.entity.Member;

public record CommentSaveReqDto(
        Long boardId,
        String comment
) {
    public Comment toEntity(Member member, Board board) {
        return Comment.builder()
                .comment(comment)
                .writer(member)
                .board(board)
                .build();
    }
}
