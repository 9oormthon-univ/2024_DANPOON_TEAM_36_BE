package univ.yesummit.domain.board.api.dto.response;

import lombok.Builder;
import univ.yesummit.domain.board.domain.Board;
import univ.yesummit.domain.board.domain.BoardPicture;
import univ.yesummit.domain.comment.api.dto.response.CommentInfoResDto;
import univ.yesummit.domain.member.entity.Member;

import java.util.List;

@Builder
public record BoardInfoResDto(
        Long myMemberId,
        Long writerMemberId,
        String writerMemberName,
        Long boardId,
        String title,
        String content,
        List<String> imageUrl,
        int likeCount,
        boolean isLike,
        int InvestmentCount,
        boolean invest,
        int commentCount,
        String date,
        List<CommentInfoResDto> comments
) {
    public static BoardInfoResDto of(Member member, Board board, boolean isLike) {
        List<String> imageUrl = board.getPictures().stream()
                .map(BoardPicture::getImageUrl)
                .toList();

        return BoardInfoResDto.builder()
                .myMemberId(member.getId())
                .writerMemberId(board.getWriter().getId())
                .writerMemberName(board.getWriter().getUsername())
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .content(board.getContent())
                .imageUrl(imageUrl)
                .likeCount(board.getLikeCount())
                .commentCount(board.getComments().size())
                .date(board.getBoardDate())
                .build();
    }
}
