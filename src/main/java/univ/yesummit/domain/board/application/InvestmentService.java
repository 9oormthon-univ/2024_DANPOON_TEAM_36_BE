package univ.yesummit.domain.board.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import univ.yesummit.domain.board.api.dto.response.InvestmentResDto;
import univ.yesummit.domain.board.domain.Board;
import univ.yesummit.domain.board.domain.Investment;
import univ.yesummit.domain.board.domain.repository.BoardRepository;
import univ.yesummit.domain.board.domain.repository.InvestmentRepository;
import univ.yesummit.domain.member.entity.Member;
import univ.yesummit.domain.member.repository.MemberRepository;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InvestmentService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final InvestmentRepository investmentRepository;

    @Transactional
    public void addInvestment(Long memberId, Long boardId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        if (investmentRepository.existsByBoardAndMember(board, member)) {
            throw new IllegalStateException("이미 투자 제안 중입니다.");
        }

        board.updateInvestmentCount();
        investmentRepository.save(Investment.builder()
                .board(board)
                .member(member)
                .build());
    }

    @Transactional
    public void cancelInvestment(Long memberId, Long boardId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        Investment investment = investmentRepository.findByBoardAndMember(board, member)
                .orElseThrow(() -> new IllegalArgumentException("투자 기록이 존재하지 않습니다."));

        board.cancelInvestmentCount();
        investmentRepository.delete(investment);
    }

    public List<InvestmentResDto> getInvestedBoardsWithDetailsByMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        List<Investment> investments = investmentRepository.findByMember(member);
        return investments.stream()
                .map(investment -> {
                    Board board = investment.getBoard();
                    return InvestmentResDto.of(
                            member.getId(),
                            member.getUsername(),
                            member.getPosition(),
                            member.getPhoneNumber(),
                            member.getEmail(),
                            board.getBoardId(),
                            board.getTitle(),
                            board.getContent(),
                            board.getInvestmentCount()
                    );
                })
                .collect(Collectors.toList());
    }
}
//@Service
//@RequiredArgsConstructor
//@Transactional(readOnly = true)
//public class InvestmentService {
//
//    private final MemberRepository memberRepository;
//    private final BoardRepository boardRepository;
//    private final InvestmentRepository investmentRepository;
//
//
//    // 투자하기 버튼 클릭
//    @Transactional
//    public void addInvestment(Long memberId, Board boardId) {
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
//
//        Board board = boardRepository.findById(boardId.getBoardId())
//                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
//
//        if (!investmentRepository.existsByBoardAndMember(board, member)) {
//            throw new IllegalStateException("이미 투자 제안 중입니다.");
//        }
//
//        board.updateInvestmentCount();
//        investmentRepository.save(Investment.builder()
//                .board(board)
//                .member(member)
//                .build());
//    }
//
//    // 투자하기 취소
//    @Transactional
//    public void cancelInvestment(Long memberId, Board boardId) {
//
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
//
//        Board board = boardRepository.findById(boardId.getBoardId())
//                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
//
//        if (!investmentRepository.existsByBoardAndMember(board, member)) {
//            throw new IllegalStateException("투자 제안 중이 아닙니다.");
//        }
//
//        Investment investment = investmentRepository.findByBoardAndMember(board, member)
//                .orElseThrow(() -> new IllegalArgumentException("좋아요 정보가 없습니다."));
//
//        board.cancelInvestmentCount();
//        investmentRepository.delete(investment);
//    }
//
//    @Transactional
//    public List<InvestmentResDto> getAllInvestment(Long memberId, Long boardId) {
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
//
//        Board board = boardRepository.findById(boardId)
//                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
//
//        // 해당 게시글에 대한 모든 투자 제안 조회
//        List<Investment> investments = investmentRepository.findByBoard(board);
//        return investments.stream()
//                .map(investment -> {
//                    Member investor = investment.getMember(); // 투자자 정보 가져오기
//                    return new InvestmentResDto(
//                            investor.getId(),
//                            investor.getUsername(),
//                            investor.getPosition(),
//                            investor.getPhoneNumber(),
//                            investor.getEmail(),
//                            boardId
//                    );
//                })
//                .collect(Collectors.toList());
//    }
//
//}
