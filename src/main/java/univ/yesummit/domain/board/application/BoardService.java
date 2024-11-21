package univ.yesummit.domain.board.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import univ.yesummit.domain.board.api.dto.request.BoardSaveReqDto;
import univ.yesummit.domain.board.api.dto.request.BoardUpdateReqDto;
import univ.yesummit.domain.board.api.dto.response.BoardInfoResDto;
import univ.yesummit.domain.board.domain.Board;
import univ.yesummit.domain.board.domain.BoardPicture;
import univ.yesummit.domain.board.domain.repository.BoardLikeRepository;
import univ.yesummit.domain.board.domain.repository.BoardPictureRepository;
import univ.yesummit.domain.board.domain.repository.BoardRepository;
import univ.yesummit.domain.member.entity.Member;
import univ.yesummit.domain.member.repository.MemberRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardPictureRepository boardPictureRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final MemberRepository memberRepository;

    // 게시글 저장
    @Transactional
    public Long boardSave(Long id, BoardSaveReqDto boardSaveReqDto) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."+ id));
        Board board = boardSaveReqDto.toEntity(member);

        if (board == null) {
            throw new IllegalStateException("게시글 객체 생성 실패.");
        }

        boardImageSave(board, boardSaveReqDto);
        Board saveBoard = boardRepository.save(board);

        if (saveBoard == null) {
            throw new IllegalStateException("게시글 저장에 실패했습니다.");
        }

        return saveBoard.getBoardId();

    }

    private void boardImageSave(Board board, BoardSaveReqDto boardSaveReqDto) {
        for (String imageUrl : boardSaveReqDto.imageUrl()) {
            boardPictureRepository.save(BoardPicture.builder()
                    .board(board)
                    .imageUrl(imageUrl)
                    .build());
        }
    }

    // 게시글 전체 조회
    @Transactional
    public List<BoardInfoResDto> allBoardInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        List<Board> boards = boardRepository.findAll();
        return boards.stream()
                .map(board -> BoardInfoResDto.of(member, board, boardLikeRepository.existsByBoardAndMember(board, member)))
                .collect(Collectors.toList());
    }

    // 게시글 한개 조회
    @Transactional
    public BoardInfoResDto boardInfo(Long id, Long boardId) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        checkBoardOwnership(member, board);

        boolean isLike = boardLikeRepository.existsByBoardAndMember(board, member);

        return BoardInfoResDto.of(member, board, isLike);
    }

    // 게시글 수정
    @Transactional
    public BoardInfoResDto boardUpdate(Long id, Long boardId, BoardUpdateReqDto boardUpdateReqDto) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        checkBoardOwnership(member, board);

        board.boardUpdate(boardUpdateReqDto);

        // 새로운 이미지 url만 받아서 추가한다.
        for (String url : boardUpdateReqDto.newImageUrl()) {
            boardPictureRepository.save(BoardPicture.builder()
                    .board(board)
                    .imageUrl(url)
                    .build());
        }

        boolean isLike = boardLikeRepository.existsByBoardAndMember(board, member);

        return BoardInfoResDto.of(member, board, isLike);

    }

    // 게시글 삭제
    @Transactional
    public void boardDelete(Long id, Long boardId) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        checkBoardOwnership(member, board);

        boardPictureRepository.deleteByBoardBoardId(boardId);
        boardRepository.delete(board);

    }

    // 게시글 소유권 확인
    private void checkBoardOwnership(Member member, Board board) {
        if (!member.getId().equals(board.getWriter().getId())) {
            throw new IllegalStateException("본인이 작성한 게시글만 수정 및 삭제 할 수 있습니다.");
        }
    }

    public Board findById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
    }

}
