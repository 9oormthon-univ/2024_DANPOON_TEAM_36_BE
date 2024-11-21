package univ.yesummit.domain.comment.api.dto.response;

import lombok.Builder;
import univ.yesummit.domain.comment.domain.Comment;

@Builder
public record CommentInfoResDto(
        Long writerMemberId,
        Long commentId,
        String comment
) {
    public static CommentInfoResDto of(Comment comment) {
        return CommentInfoResDto.builder()
                .writerMemberId(comment.getWriter().getId())
                .commentId(comment.getCommentId())
                .comment(comment.getComment())
                .build();
    }
}
