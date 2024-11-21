package univ.yesummit.domain.board.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import univ.yesummit.domain.board.api.dto.response.BoardLikeResDto;
import univ.yesummit.domain.board.domain.Board;
import univ.yesummit.domain.board.domain.BoardLike;
import univ.yesummit.domain.board.domain.BoardPicture;
import univ.yesummit.domain.board.domain.repository.BoardLikeRepository;
import univ.yesummit.domain.board.domain.repository.BoardRepository;
import univ.yesummit.domain.member.entity.Member;
import univ.yesummit.domain.member.repository.MemberRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardLikeService {

    private final BoardLikeRepository boardLikeRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    // 게시글 좋아요
    @Transactional
    public void addBoardLike(Long memberId, Board boardId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        Board board = boardRepository.findById(boardId.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        if (boardLikeRepository.existsByBoardAndMember(board, member)) {
            throw new IllegalStateException("이미 존재하는 좋아요입니다.");
        }

        boardLikeRepository.save(BoardLike.builder()
                .board(board)
                .member(member)
                .build());

        board.updateLikeCount();

    }


    // 게시글 좋아요 취소
    @Transactional
    public void cancelBoardLike(Long memberId, Board boardId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        Board board = boardRepository.findById(boardId.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        if (!boardLikeRepository.existsByBoardAndMember(board, member)) {
            throw new IllegalStateException("이미 존재하는 좋아요가 없습니다.");
        }

        BoardLike boardLike = boardLikeRepository.findByBoardAndMember(board, member)
                .orElseThrow(() -> new IllegalArgumentException("좋아요 정보가 없습니다."));

        boardLikeRepository.delete(boardLike);

        board.cancelLikeCount();
    }

    // 내가 좋아요 누른 게시글
    @Transactional
    public List<BoardLikeResDto> findMyLikedBoards(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        List<BoardLike> likedBoards = boardLikeRepository.findByMember(member);

        return likedBoards.stream()
                .map(boardLike -> {
                    Board board = boardLike.getBoard();
                    return new BoardLikeResDto(
                            memberId,
                            board.getWriter().getId(),
                            board.getBoardId(),
                            board.getTitle(),
                            board.getContent(),
                            board.getPictures().stream().map(BoardPicture::getImageUrl).collect(Collectors.toList())
                    );
                })
                .collect(Collectors.toList());
    }


}
