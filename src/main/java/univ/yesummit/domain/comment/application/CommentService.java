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
    public void commentSave(String email, CommentSaveReqDto commentSaveReqDto) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        Board board = boardRepository.findById(commentSaveReqDto.boardId())
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        commentRepository.save(commentSaveReqDto.toEntity(member, board));
    }

    // 댓글 수정
    @Transactional
    public CommentInfoResDto commentUpdate(String email, Long commentId, CommentUpdateReqDto commentUpdateReqDto){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));

        comment.updateComment(commentUpdateReqDto.comment());

        return CommentInfoResDto.of(comment);
    }

    // 댓글 삭제
    @Transactional
    public void commentDelete(String email, Long commentId) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));

        commentRepository.delete(comment);
    }
}