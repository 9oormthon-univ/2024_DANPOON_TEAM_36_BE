package univ.yesummit.domain.board.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import univ.yesummit.domain.board.domain.Board;
import univ.yesummit.domain.board.domain.Investment;
import univ.yesummit.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface InvestmentRepository extends JpaRepository<Investment, Long> {

    boolean existsByBoardAndMember(Board board, Member member);

    Optional<Investment> findByBoardAndMember(Board board, Member member);

    List<Investment> findByMember(Member member);

    List<Investment> findByBoard(Board board);

    // 게시글 작성자 ID로 투자 조회
    List<Investment> findAllByBoardWriterId(Long writerId);
}
