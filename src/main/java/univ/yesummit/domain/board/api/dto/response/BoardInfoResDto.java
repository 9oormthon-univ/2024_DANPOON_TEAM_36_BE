package univ.yesummit.domain.board.api.dto.response;

import lombok.Builder;
import univ.yesummit.domain.board.domain.Board;
import univ.yesummit.domain.board.domain.BoardPicture;
import univ.yesummit.domain.comment.api.dto.response.CommentInfoResDto;
import univ.yesummit.domain.member.entity.Member;

import java.time.format.DateTimeFormatter;
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
        String serviceUrl,
        String PTUrl,
        int likeCount,
        boolean isLike,
        int InvestmentCount,
        boolean invest,
        int commentCount,
        String date,
        List<CommentInfoResDto> comments
) {
    public static BoardInfoResDto of(Member member, Board board, boolean isLike) {
        // 이미지 URL 변환
        List<String> imageUrl = board.getPictures().stream()
                .map(BoardPicture::getImageUrl)
                .toList();

        // 날짜 포맷 설정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = board.getBoardDate().format(formatter);

        return BoardInfoResDto.builder()
                .myMemberId(member.getId())
                .writerMemberId(board.getWriter().getId())
                .writerMemberName(board.getWriter().getUsername())
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .content(board.getContent())
                .imageUrl(imageUrl)
                .serviceUrl(builder().serviceUrl)
                .PTUrl(builder().PTUrl)
                .likeCount(board.getLikeCount())
                .isLike(isLike)
                .commentCount(board.getComments().size())
                .date(formattedDate) // 포맷팅된 날짜 전달
                .build();
    }
}

//
//@Builder
//public record BoardInfoResDto(
//        Long myMemberId,
//        Long writerMemberId,
//        String writerMemberName,
//        Long boardId,
//        String title,
//        String content,
//        List<String> imageUrl,
//        String serviceUrl,
//        String PTUrl,
//        int likeCount,
//        boolean isLike,
//        int InvestmentCount,
//        boolean invest,
//        int commentCount,
//        String date,
//        List<CommentInfoResDto> comments
//) {
//    public static BoardInfoResDto of(Member member, Board board, boolean isLike) {
//        List<String> imageUrl = board.getPictures().stream()
//                .map(BoardPicture::getImageUrl)
//                .toList();
//
//        return BoardInfoResDto.builder()
//                .myMemberId(member.getId())
//                .writerMemberId(board.getWriter().getId())
//                .writerMemberName(board.getWriter().getUsername())
//                .boardId(board.getBoardId())
//                .title(board.getTitle())
//                .content(board.getContent())
//                .imageUrl(imageUrl)
//                .serviceUrl(builder().serviceUrl)
//                .PTUrl(builder().PTUrl)
//                .likeCount(board.getLikeCount())
//                .commentCount(board.getComments().size())
//                .date(board.getBoardDate())
//                .build();
//    }
//}
