package univ.yesummit.domain.comment.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import univ.yesummit.domain.board.domain.Board;
import univ.yesummit.domain.board.domain.repository.BoardRepository;
import univ.yesummit.domain.comment.api.dto.request.CommentSaveReqDto;
import univ.yesummit.domain.comment.api.dto.request.CommentUpdateReqDto;
import univ.yesummit.domain.comment.api.dto.response.CommentInfoResDto;
import univ.yesummit.domain.comment.domain.Comment;
import univ.yesummit.domain.comment.domain.repository.CommentRepository;
import univ.yesummit.domain.member.entity.Member;
import univ.yesummit.domain.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    // 댓글 저장
    @Transactional
    public void commentSave(Long id, CommentSaveReqDto commentSaveReqDto) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        Board board = boardRepository.findById(commentSaveReqDto.boardId())
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        commentRepository.save(commentSaveReqDto.toEntity(member, board));
    }

    // 댓글 수정
    @Transactional
    public CommentInfoResDto commentUpdate(Long id, Long commentId, CommentUpdateReqDto commentUpdateReqDto){
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));

        checkCommentOwnership(member, comment);

        comment.updateComment(commentUpdateReqDto.comment());

        return CommentInfoResDto.of(comment);
    }

    // 댓글 삭제
    @Transactional
    public void commentDelete(Long id, Long commentId) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));

        checkCommentOwnership(member, comment);

        commentRepository.delete(comment);
    }

    // 댓글 소유권 확인
    private void checkCommentOwnership(Member member, Comment comment) {
        if (!member.getId().equals(comment.getWriter().getId())) {
            throw new IllegalStateException("본인이 작성한 댓글만 수정 및 삭제 할 수 있습니다.");
        }
    }
}
