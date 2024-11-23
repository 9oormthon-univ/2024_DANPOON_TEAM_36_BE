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
import univ.yesummit.domain.feed.entity.Feed;
import univ.yesummit.domain.feed.repository.FeedRepository;
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
    private final FeedRepository feedRepository;

    // 게시글 저장
    @Transactional
    public Long boardSave(Long memberId, BoardSaveReqDto boardSaveReqDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다. " + memberId));

        Board board = boardSaveReqDto.toEntity(member);

        List<Feed> feeds = getFeedsByMember(member);
        boardImageSave(board, boardSaveReqDto.imageUrl(), feeds);

        Board savedBoard = boardRepository.save(board);
        return savedBoard.getBoardId();
    }

    // Feed 데이터를 가져오는 헬퍼 메서드
    private List<Feed> getFeedsByMember(Member member) {
        return feedRepository.findAllByMember(member);
    }


    private void boardImageSave(Board board, List<String> imageUrls, List<Feed> feeds) {
        for (String imageUrl : imageUrls) {
            boardPictureRepository.save(BoardPicture.builder()
                    .board(board)
                    .imageUrl(imageUrl) // URL 직접 저장
                    .build());
        }
        for (Feed feed : feeds) {
            boardPictureRepository.save(BoardPicture.builder()
                    .board(board)
                    .feed(feed) // Feed에서 이미지 추출하여 저장
                    .build());
        }
    }


    @Transactional
    public List<BoardInfoResDto> allBoardInfoBySummitId(Long summitId, Long memberId) {
        List<Board> boards = boardRepository.findBySummitId(summitId);
        Member member = memberId != null ? memberRepository.findById(memberId).orElse(null) : null; // memberId로 member 조회
        return boards.stream()
                .map(board -> BoardInfoResDto.of(member, board, false)) // member 정보를 전달
                .collect(Collectors.toList());
    }

    // 게시글 한개 조회
    @Transactional
    public BoardInfoResDto boardInfo(Long boardId, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        return BoardInfoResDto.of(member, board, false);
    }

    // (내가 작성한) 게시글 한개 조회
    @Transactional
    public BoardInfoResDto myBoardInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        Board board = boardRepository.findFirstByWriter(member)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        boolean isLike = boardLikeRepository.existsByBoardAndMember(board, member);
        return BoardInfoResDto.of(member, board, isLike);
    }

    // 게시글 수정
    @Transactional
    public BoardInfoResDto boardUpdate(Long memberId, Long boardId, BoardUpdateReqDto boardUpdateReqDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        checkBoardOwnership(member, board);

        board.boardUpdate(boardUpdateReqDto);

        // 새로운 이미지 url만 받아서 추가
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
    public void boardDelete(Long memberId, Long boardId) {
        Member member = memberRepository.findById(memberId)
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
